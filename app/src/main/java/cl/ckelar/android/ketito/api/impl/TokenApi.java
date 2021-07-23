package cl.ckelar.android.ketito.api.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cl.ckelar.android.ketito.api.ITokenApi;
import cl.ckelar.android.ketito.dto.api.TokenData;
import cl.ckelar.android.ketito.helpers.tags.TagApi;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class TokenApi {

    private static final String TAG = "GetTokenApi";

    /**
     * Obtiene el listado de Token asociados al usuario
     *
     * @param contentType es la codificación de los parámetros
     * @param authorization contiene el dato bearer + " " + token
     *
     * @return List<TokenData>
     * **/

    public List<TokenData> GetUsersToken(String contentType, String authorization){

        List<TokenData> listTokens = new ArrayList<>();

      try{
            Log.i(TAG, "--- METHOD: getTokens ---");

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
            ITokenApi iTokenApi = retrofit.create(ITokenApi.class);

            contentType = TagApi.API_HEADER_CONTENT_TYPE_VALUE;

            Call<ResponseBody> call = iTokenApi.getToken(contentType, authorization);
            retrofit2.Response<ResponseBody> responseBody = call.execute();

            Log.i(TAG, call.request().url().toString());

            if (responseBody.isSuccessful()) {

                Log.i(TAG, "--- Obtiene los token asociados al usuario ---");

                try {

                    String response = responseBody.body().string();

                    Log.d(TAG, response);   // Debug

                    Gson gson = new Gson();

                    Type collectionType = new TypeToken<List<TokenData>>(){}.getType();
                    listTokens = gson.fromJson(response, collectionType);

                    Log.i(TAG, "--- Decodifica el JSON ---");

                }
                catch (Exception ex) {
                    Log.e(TAG, "--- ERROR al decodificar el JSON Login ---");
                    Log.e(TAG, ex.toString());
                    listTokens = null;
                }

            } else {
                try {

                    String response = responseBody.errorBody().string();

                    Log.d(TAG, response);   // Debug

                    listTokens = null;

                }
                catch (Exception ex) {
                    Log.i(TAG, "--- ERROR al decodificar el JSON ---");
                    listTokens = null;
                    Log.e(TAG, ex.toString());
                }

                Log.e(TAG, "--- ERROR al consultar el Servicio ---");
                listTokens = null;

            }

        } catch (Exception e) {

            Log.e(TAG, "--- ERROR al consultar ---");
            Log.e(TAG, e.toString());
            listTokens = null;
        }

        /*
        //TokenData de prueba, con los datos de la bd, esto cambiaría luego
        TokenData tknData = new TokenData();
        tknData.setActivo(true);
        tknData.setApiUser(true);
        tknData.setIdEmpresa("mdTie19en1");
        tknData.setNombre("prueba_2");
        tknData.setToken("BOzUk7PYnd2EV3V32hqD4NL0pxqd0wt5BFZW5v9sbAzxuI3JawVAsA-17XoYBUmzRuDnp9HXF4hWwhpYzq_HkJGdyB2gnSWK2tOAcpAM6brYjUQVfEicWIgLxSWGBisaxAPSX2W4hzxLxXFfnic5K8-ie67tX6M_0TFE-qqDztGBm0lhLaA5PAbhj7sljRBSNY3cnhkJxnAzNQLNhXnBkQhH2A4kHXoDIYhzWz7G-xJ7svCx6DoITX1_7gq7QSgf_UJ6AeTXXxgJI7XMDLMaoFbeOVZzxCTWDKl3f31seDQwCEiYJcsGM6HzwtTKFEVY3_-wRLtPC_TBuzcdamL0sFwjCHwvxd5Gaxnc1dbVWkAUEms7rPPE55B_tLxBSKWkQnipnI0MN3vhwXOcn9iThRLqXb9mD_i1Fv87RDEuptwQqJPfnddm8jWravAmqqd_5SkMBkmYejLMBd2ygSYVn7qmi0V1KL6Cfw4BOyz4sLlFWe3UQeTsIGuWpcB3KSM1Coo_W6sQuhBJlQr7wcEy8-aQe5yyPjVD93sPRt2KIBg");
        tknData.setUserId("cb49386d-9e91-4d8a-b63e-7da6fa8ee43c");
        tknData.setUserTotem("carl.edlv@gmail.com");
        tknData.setUserName("carl.edlv@gmail.com"); */

        return listTokens;


    }

    /*
    public TokenData findUserToken(String contentType, String authorization, String idToken) {

        TokenData tokenData = new TokenData();

        try {

            Log.i(TAG, "--- METHOD: findToken ---");

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
            ITokenApi iTokenApi = retrofit.create(ITokenApi.class);

            // Define el valor por defecto del grant_type
            contentType = TagApi.API_HEADER_CONTENT_TYPE_VALUE;

            Call<ResponseBody> call = iTokenApi.findToken(contentType, authorization, idToken);
            retrofit2.Response<ResponseBody> responseBody = call.execute();

            Log.i(TAG, call.request().url().toString());

            if (responseBody.isSuccessful()) {

                Log.i(TAG, "--- Obtiene los datos del token elegido ---");
                Log.i(TAG, "RESPONSEBODY: " + responseBody.toString());

                try {

                    String response = responseBody.body().string();

                    Log.d(TAG, response);   // Debug

                    Gson gson = new Gson();
                    tokenData = gson.fromJson(response, TokenData.class);

                    Log.i(TAG, "--- Decodifica el JSON ---");

                }
                catch (Exception ex) {
                    Log.e(TAG, "--- ERROR al decodificar el JSON ---");
                    Log.e(TAG, ex.toString());
                    tokenData = null;
                }

            } else {
                tokenData = null;
            }

        } catch (Exception e) {

            Log.e(TAG, "--- ERROR al consultar ---");
            Log.e(TAG, e.toString());
            tokenData = null;
        }

        return tokenData;

    }*/

}
