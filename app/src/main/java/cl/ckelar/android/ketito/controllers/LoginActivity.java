package cl.ckelar.android.ketito.controllers;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.api.impl.LoginApi;
import cl.ckelar.android.ketito.dto.TokenC;
import cl.ckelar.android.ketito.dto.UserSesion;
import cl.ckelar.android.ketito.dto.api.LoginResponse;
import cl.ckelar.android.ketito.helpers.Util;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;
import cl.ckelar.android.ketito.helpers.tags.TagApi;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUser;
    private EditText edtPwd;
    private CheckBox chkPwdShow;
    private Button btnLogin;
    private TextView txtPwdRecovery;
    private TextView txtVersion;

    private String user = "";
    private String pwd = "";

    private UserSesion userSesion;
    private TokenC tokenC;

    private KetitoDatabase ketitoDatabase;

    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        edtUser = findViewById(R.id.edtUser);
        edtPwd = findViewById(R.id.edtPassword);
        chkPwdShow = findViewById(R.id.chkPwdShow);
        btnLogin = findViewById(R.id.btnLogin);
        txtPwdRecovery = findViewById(R.id.txtPwdRecovery);
        txtVersion = findViewById(R.id.txtVersion);

        ketitoDatabase = ketitoDatabase.getInstance(LoginActivity.this);

        userSesion = ketitoDatabase.userSesionDao().getUserSesionFirst();

        //versionApp
        String auxText = "Versión: " + Util.getVersionName(this);
        txtVersion.setText(auxText);


        //userSesion
        if (userSesion != null) {
            edtUser.setText(userSesion.getUserName());
            Log.d("TOKEN: ", userSesion.getAccess_token());
        }
        else {
            userSesion = new UserSesion();
        }

        chkPwdShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                if (chkPwdShow.isChecked()) {
                    edtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    edtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSesion userAux = ketitoDatabase.userSesionDao().getUserSesionFirst();
                if(userAux !=null){
                    TokenC tokenAux = ketitoDatabase.tokenDao().getTokenByToken(userAux.getAccess_token());
                    if(tokenAux == null){
                        ketitoDatabase.userSesionDao().deleteUserSesion(userSesion);
                    }
                }


                user = (edtUser.getText().toString().length() > 0 && !edtUser.getText().toString().isEmpty()) ? edtUser.getText().toString() : "";
                pwd = (edtPwd.getText().toString().length() > 0 && !edtPwd.getText().toString().isEmpty()) ? edtPwd.getText().toString() : "";

                if (user.isEmpty()) {
                    //
                    edtUser.setError(getResources().getString(R.string.lbl_msg_error_empty));
                }
                else if (pwd.isEmpty()) {
                    //
                    edtPwd.setError(getResources().getString(R.string.lbl_msg_error_empty));
                }
                else {
                    new AsyncLogin().execute();
                }

            }
        });


        txtPwdRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(TagApi.API_URL_PWD_FORGOT)));
            }
        });


    }


    /**
     * Genera la conexión con la clase LoginAPI de manera asincrónica.
     * Retorna un nuevo activity y redirige a esta dirección si el login es exitoso.
     *
     *
     *
     */
    private class AsyncLogin extends AsyncTask<String, String, String> {

        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;

        LoginApi loginApi;
        LoginResponse loginResponse;

        boolean isSaved = false;
        String error_message = "";

        @Override
        protected void onPreExecute() {

            dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_loading_layout, null);
            dialogBuilder.setView(dialogView);
            alertDialog = dialogBuilder.create();
            alertDialog.show();
        }

        @Override
        protected void onProgressUpdate(String... text) {
            //finalResult.setText(text[0]);
        }

        @Override
        protected String doInBackground(String... params) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()

            //verificamos si existe un token anterior
            tokenC= ketitoDatabase.tokenDao().getTokenBySelected();
            UserSesion auxUser = ketitoDatabase.userSesionDao().getUserSesionFirst();

            if(tokenC != null){
                    UserSesion userAux = ketitoDatabase.userSesionDao().getUserSesion(tokenC.getToken());
                if(userAux != null){
                    userSesion = ketitoDatabase.userSesionDao().getUserSesionFirst();
                    isSaved = true;
                }else{
                    isSaved = false;
                    error_message = "Se produjo un error inesperado al iniciar sesión.";
                }
            }else{
                ketitoDatabase.userSesionDao().deleteUserSesion(userSesion);

                loginResponse = new LoginResponse();
                loginApi = new LoginApi();
                loginResponse = loginApi.login(user, pwd);

                if (loginResponse.getLogin() != null) {
                    userSesion.setAccess_token(loginResponse.getLogin().getAccessToken());
                    userSesion.setUserName(loginResponse.getLogin().getUserName());
                    userSesion.setToken_type(loginResponse.getLogin().getTokenType());
                    userSesion.setExpires_in(loginResponse.getLogin().getExpiresIn());
                    userSesion.setIssued(loginResponse.getLogin().getIssued());
                    userSesion.setExpires(loginResponse.getLogin().getExpires());

                    ketitoDatabase.userSesionDao().insertUserSesion(userSesion);

                    UserSesion userSesion2 = new UserSesion();
                    userSesion2 = ketitoDatabase.userSesionDao().getUserSesion(userSesion.getAccess_token());

                    if (userSesion2 != null) {
                        isSaved = true;
                    }
                    else {
                        isSaved = false;
                        error_message = "Se produjo un error inesperado al iniciar sesión.";
                    }
                }
                else if (loginResponse.getLoginError() != null) {
                    isSaved = false;
                    error_message = loginResponse.getLoginError().getErrorDescription();
                }
                else {
                    isSaved = false;
                    error_message = "Se produjo un error inesperado al consultar el servicio.";
                }
            }

                //
                return null;

        }


        @Override
        protected void onPostExecute(String result) {
            alertDialog.cancel();

            if (isSaved) {
                Intent intent = new Intent(LoginActivity.this, BusinessSelectActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Util.errorAppDialog(LoginActivity.this, error_message);
            }

        }

    }


}
