package cl.ckelar.android.ketito;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

import java.util.List;

import cl.ckelar.android.ketito.controllers.BusinessSelectActivity;
import cl.ckelar.android.ketito.controllers.DashboardActivity;
import cl.ckelar.android.ketito.controllers.LoginActivity;
import cl.ckelar.android.ketito.controllers.MenuEncuestasActivity;
import cl.ckelar.android.ketito.controllers.SettingSelectActivity;
import cl.ckelar.android.ketito.controllers.WithoutChartActivity;
import cl.ckelar.android.ketito.dto.Empresa;
import cl.ckelar.android.ketito.dto.Encuesta;
import cl.ckelar.android.ketito.dto.Preguntas;
import cl.ckelar.android.ketito.dto.Respuesta;
import cl.ckelar.android.ketito.dto.SettingUser;
import cl.ckelar.android.ketito.dto.TokenC;
import cl.ckelar.android.ketito.dto.Toma;
import cl.ckelar.android.ketito.dto.Ubicacion;
import cl.ckelar.android.ketito.dto.UserSesion;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;

public class MainActivity extends AppCompatActivity {

    private KetitoDatabase ketitoDatabase;
    private UserSesion userSesion;
    private TokenC tokenC;
    private Ubicacion ubicacion;
    private Empresa empresa;
    private TokenC tokenAux;
    private Encuesta encuesta;


    private Button btnIniciarEncuesta;
    private Button btnCharts;
    private TextView txtBusinessWelcome;
    private TextView txtTokenWelcome;
    private ImageButton imgBtnView;
    private Button btnSettings;
    private ImageButton imgBtnLogout;
    private Button btnSalir;
    private ConstraintLayout headerLayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.drawable.ketito);
        //getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.header));

        btnIniciarEncuesta = findViewById(R.id.btnIniciarEncuesta);
        txtBusinessWelcome = findViewById(R.id.txtBusinessWelcome);
        txtTokenWelcome = findViewById(R.id.txtTokenWelcome);
        imgBtnView = findViewById(R.id.imgBtnLogo);
        btnSettings = findViewById(R.id.btnConfig);
        imgBtnLogout = findViewById(R.id.imgBtnLogout);
        btnSalir = findViewById(R.id.btnSalir);
        headerLayout = findViewById(R.id.HeaderLayout);
        btnCharts = findViewById(R.id.btnCharts);


        ketitoDatabase = ketitoDatabase.getInstance(MainActivity.this);     // Inicia la conexión con la base de datos local
        userSesion = ketitoDatabase.userSesionDao().getUserSesionFirst();   // Obtiene el usuario con el cual se está trabajando
        ubicacion = ketitoDatabase.ubicacionDao().getUbicacionBySelected(true);
        empresa = ketitoDatabase.empresaDao().getEmpresaBySelected();
        tokenAux = ketitoDatabase.tokenDao().getTokenBySelected();
        encuesta = ketitoDatabase.encuestaDao().getEncuestaBySelected(true);


        if (userSesion == null || ubicacion == null || empresa == null || tokenAux== null ||encuesta == null) {
            // si no existe un usuario logueado, abre la pantalla de Login
            Intent iAccount = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(iAccount);
            finish();
        } else {
                tokenC = ketitoDatabase.tokenDao().getTokenBySelected();
                if(tokenC != null){
                    Ubicacion ubi = ketitoDatabase.ubicacionDao().getUbicacionBySelected(true);
                    String ubiText = txtTokenWelcome.getText().toString();
                    ubiText = ubiText.replace("%ubi", ubi.getUbicacion());
                    txtTokenWelcome.setText(ubiText);

                    String businessText = txtBusinessWelcome.getText().toString();

                    Empresa empresa = ketitoDatabase.empresaDao().getEmpresaBySelected();
                    //Logo

                    if(empresa.getRutaImagen() != null && empresa.getRutaImagen().length() > 0){
                        String url = empresa.getRutaImagen();
                        Picasso.get().load(url).into(imgBtnView);
                    }

                    if (empresa.getRazon() != null) {
                        businessText = businessText.replace("%business", empresa.getRazon());
                    } else {
                        businessText = businessText.replace("%business", "No Seleccionada");
                    }

                    txtBusinessWelcome.setText(businessText);

                }else{


                    Ubicacion ubi = ketitoDatabase.ubicacionDao().getUbicacionBySelected(true);
                    String ubiText = txtTokenWelcome.getText().toString();
                    ubiText = ubiText.replace("@ubi", ubi.getUbicacion());
                    txtTokenWelcome.setText(ubiText);

                    String businessText = txtBusinessWelcome.getText().toString();
                    Empresa empresa = ketitoDatabase.empresaDao().getEmpresaBySelected();

                    if (empresa != null) {
                        businessText = businessText.replace("%business", empresa.getRazon());
                    } else {
                        businessText = businessText.replace("%business", "No Seleccionada");
                    }

                    txtBusinessWelcome.setText(businessText);
                }

        }


        imgBtnLogout.setOnClickListener(v -> cerrarSesion());
        btnSalir.setOnClickListener(v -> salirApp());

        btnSettings.setOnClickListener(v -> {
            Intent iSetting = new Intent(MainActivity.this, SettingSelectActivity.class);
            startActivity(iSetting);
            finish();
        });

        // Acción para enviar la encuesta al cliente
        btnIniciarEncuesta.setOnClickListener(view -> {
            Intent iPoll = new Intent(MainActivity.this, MenuEncuestasActivity.class);
            startActivity(iPoll);
            finish();
        });



        List<Respuesta> repuestasList = ketitoDatabase.respuestasDao().getAll();



        // Acción para enviar la encuesta al cliente
        btnCharts.setOnClickListener(view -> {

            if(repuestasList.size() > 0){
                Intent iPoll = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(iPoll);
                finish();
            }else{
                Intent iPoll = new Intent(MainActivity.this, WithoutChartActivity.class);
                startActivity(iPoll);
                finish();
            }



        });
    }

    /**
     * Método que permite desconectarse de la aplicación eliminando los datos guardados del usuario.
     * Envía una alerta solicitando confirmación
     *
     */
    private void cerrarSesion() {
        // un listener que al pulsar, cierre la aplicacion
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.error_icon)
                .setTitle("¿Realmente desea cerrar sesión?")
                .setMessage("Si cierra sesión deberá elegir nuevamente su Empresa y Token asociado.")
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    //android.os.Process.killProcess(android.os.Process.myPid()); //Su funcion es algo similar a lo que se llama cuando se presiona el botón "Forzar Detención" o "Administrar aplicaciones", lo cuál mata la aplicación
                    //finish(); Si solo quiere mandar la aplicación a segundo plano
                    userSesion = ketitoDatabase.userSesionDao().getUserSesionFirst();
                   try{
                       TokenC auxTokenCDel = ketitoDatabase.tokenDao().getTokenBySelected();
                       ketitoDatabase.userSesionDao().deleteUserSesion(userSesion);
                       ketitoDatabase.tokenDao().deleteTokenC(auxTokenCDel);
                       Intent intentLogin = new Intent(MainActivity.this, BusinessSelectActivity.class);
                       startActivity(intentLogin);
                       finish();
                   }catch(Exception ex){
                       throw ex;
                   }
                }).show();
    }

    /**
     * Método que permite salir de la aplicación sin eliminar los datos guardados del usuario.
     * Envía una alterta solicitando confirmación
     */
    private void salirApp() {
        // un listener que al pulsar, cierre la aplicacion
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.error_icon)
                .setTitle("¿Realmente desea cerrar la aplicación?")
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    finishAffinity();
                    android.os.Process.killProcess(android.os.Process.myPid()); //Su funcion es algo similar a lo que se llama cuando se presiona el botón "Forzar Detención" o "Administrar aplicaciones", lo cuál mata la aplicación
                    //finish(); Si solo quiere mandar la aplicación a segundo plano
                }).show();
    }
}
