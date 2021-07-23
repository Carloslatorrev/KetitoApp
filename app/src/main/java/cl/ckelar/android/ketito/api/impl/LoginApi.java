package cl.ckelar.android.ketito.api.impl;

import android.util.Log;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import cl.ckelar.android.ketito.api.ILoginApi;
import cl.ckelar.android.ketito.dto.api.Login;
import cl.ckelar.android.ketito.dto.api.LoginError;
import cl.ckelar.android.ketito.dto.api.LoginResponse;
import cl.ckelar.android.ketito.helpers.tags.TagApi;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class LoginApi {

    private static final String TAG = "LoginApi";

    /**
     * Obtiene el token de inicio de sesión o de lo contrario un error
     *
     * @see LoginResponse
     * @see Login
     * @see LoginError
     *
     *
     * @param userName Nombre de usuario
     * @param password Contraseña de usuario
     *
     * @return token de acceso o mensaje de error
     * **/
    public LoginResponse login(String userName, String password) {

        LoginResponse loginResponse = new LoginResponse();
        Login login = new Login();
        LoginError loginError = new LoginError();

        try {

            Log.i(TAG, "--- METHOD: login ---");

            String url_base = TagApi.API_URL_BASE;

            Log.i(TAG, "URL API: " + url_base);     // Debug

            OkHttpClient.Builder b = new OkHttpClient.Builder();
            b.readTimeout(30, TimeUnit.SECONDS);
            b.writeTimeout(30, TimeUnit.SECONDS);
            b.connectTimeout(30, TimeUnit.SECONDS);
            OkHttpClient client = b.build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url_base)
                    .client(client)
                    .build();
            ILoginApi iLoginApi = retrofit.create(ILoginApi.class);

            // Define el valor por defecto del grant_type
            String grant_type = TagApi.API_PARAM_VALUE_LOGIN_GRANTTYPE;

            Call<ResponseBody> call = iLoginApi.login(grant_type, userName, password);
            retrofit2.Response<ResponseBody> responseBody = call.execute();

            Log.i(TAG, call.request().url().toString());

            if (responseBody.isSuccessful()) {

                Log.i(TAG, "--- Obtiene el token de acceso ---");

                try {

                    String response = responseBody.body().string();

                    Log.d(TAG, response);   // Debug

                    Gson gson = new Gson();

                    login = gson.fromJson(response, Login.class);

                    Log.i(TAG, "--- Decodifica el JSON ---");

                }catch (Exception ex) {
                    Log.e(TAG, "--- ERROR al decodificar el JSON Login ---");
                    Log.e(TAG, ex.toString());
                    login = null;

                    loginError.setError("error");
                    loginError.setErrorDescription("No fue posible decodificar la respuesta.");
                }

            } else {
                try {

                    String response = responseBody.errorBody().string();
                    Log.d(TAG, response);   // Debug
                    Gson gson = new Gson();
                    loginError = gson.fromJson(response, LoginError.class);

                }
                catch (Exception ex) {
                    Log.i(TAG, "--- ERROR al decodificar el JSON ---");
                    loginError.setError("error");
                    loginError.setErrorDescription("Usuario y/o Contraseña incorrecto.");
                    Log.e(TAG, ex.toString());
                }

                Log.e(TAG, "--- ERROR al consultar el Servicio ---");
                login = null;

            }

        } catch (Exception e) {

            Log.e(TAG, "--- ERROR al consultar Posts ---");
            Log.e(TAG, e.toString());
            login = null;

            loginError.setError("error");
            loginError.setErrorDescription("Se produjo un error inesperado al iniciar sesión.");
        }

        loginResponse.setLogin(login);
        loginResponse.setLoginError(loginError);

        return loginResponse;
    }

}
