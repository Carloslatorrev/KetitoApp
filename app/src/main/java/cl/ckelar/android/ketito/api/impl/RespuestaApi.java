package cl.ckelar.android.ketito.api.impl;


import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cl.ckelar.android.ketito.api.IRespuestasApi;
import cl.ckelar.android.ketito.dto.api.RespuestaData;
import cl.ckelar.android.ketito.dto.api.RespuestasListData;
import cl.ckelar.android.ketito.helpers.tags.TagApi;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RespuestaApi {

    private static final String TAG = "TomaApi";
    /**
     * Crea una Toma de la encuesta
     *
     * @param contentType codificación de los parámetros
     * @param authorization contiene el código de autorización de la API
     *
     *
     * @return <TomaData> Obtiene la respuesta de la API
     * **/
    public RespuestaData postRespuesta(String contentType, String authorization, List<RespuestasListData> listRespuesta) {

        RespuestaData respuestaResponse = new RespuestaData();

        try {

            Log.i(TAG, "--- METHOD: postToma ---");

            String url_base = TagApi.API_URL_BASE;

            Log.i(TAG, "URL API: " + url_base);     // Debug

            OkHttpClient.Builder b = new OkHttpClient.Builder();
            b.readTimeout(30, TimeUnit.SECONDS);
            b.writeTimeout(30, TimeUnit.SECONDS);
            b.connectTimeout(30, TimeUnit.SECONDS);
            OkHttpClient client = b.build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url_base)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            IRespuestasApi iRespuestaApi= retrofit.create(IRespuestasApi.class);

            //.addConverterFactory(GsonConverterFactory.create())
            // Define el valor por defecto del grant_type
            contentType = TagApi.API_HEADER_CONTENT_TYPE_VALUE2;

            Call<ResponseBody> call = iRespuestaApi.postRespuestas(contentType, authorization, listRespuesta);
            retrofit2.Response<ResponseBody> responseBody = call.execute();

            Log.i(TAG, call.request().url().toString());

            Log.i(TAG, "RESPONSEBODY: " + responseBody.toString());

            if (responseBody.isSuccessful()) {

                Log.i(TAG, "--- Obtiene el resultado del envío ---");
                //Log.i(TAG, "Código HTTP: " + responseBody.code());

                try {

                    String response = String.valueOf(responseBody.code());

                    Log.d(TAG, response);   // Debug

                    respuestaResponse.setResponse(response);

                    Log.i(TAG, "--- Decodifica el JSON ---");

                }
                catch (Exception ex) {
                    Log.e(TAG, "--- ERROR al decodificar el JSON ---");
                    Log.e(TAG, ex.toString());
                    respuestaResponse = null;
                }

            } else {
                respuestaResponse = null;
            }

        } catch (Exception e) {

            Log.e(TAG, "--- ERROR al consultar ---");
            Log.e(TAG, e.toString());
            respuestaResponse = null;
        }

        return respuestaResponse;

    }

}
