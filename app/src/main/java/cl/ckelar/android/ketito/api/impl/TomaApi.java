package cl.ckelar.android.ketito.api.impl;

import android.util.Log;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import cl.ckelar.android.ketito.api.ITomaApi;
import cl.ckelar.android.ketito.dto.api.TomaData;
import cl.ckelar.android.ketito.helpers.tags.TagApi;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class TomaApi {

    private static final String TAG = "TomaApi";
    /**
     * Crea una Toma de la encuesta
     *
     * @param contentType codificación de los parámetros
     * @param authorization contiene el código de autorización de la API
     * @param idEncuesta ID de la Encuesta
     * @param idUbicacion ID de la Ubicacion
     *
     * @return <TomaData> Obtiene la respuesta de la API
     * **/
    public TomaData getToma(String contentType, String authorization, String idUbicacion, String idEncuesta) {

        TomaData tomaResponse = new TomaData();

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
                    .client(client)
                    .build();
            ITomaApi iTomaApi= retrofit.create(ITomaApi.class);

            // Define el valor por defecto del grant_type
            contentType = TagApi.API_HEADER_CONTENT_TYPE_VALUE;

            Call<ResponseBody> call = iTomaApi.postToma(contentType, authorization, idEncuesta, idUbicacion);
            retrofit2.Response<ResponseBody> responseBody = call.execute();

            Log.i(TAG, call.request().url().toString());

            Log.i(TAG, "RESPONSEBODY: " + responseBody.toString());

            if (responseBody.isSuccessful()) {

                Log.i(TAG, "--- Obtiene el resultado del envío ---");
                //Log.i(TAG, "Código HTTP: " + responseBody.code());

                try {

                    String response = responseBody.body().string();

                    Log.d(TAG, response);   // Debug

                    Gson gson = new Gson();

                    tomaResponse = gson.fromJson(response, TomaData.class);

                    Log.i(TAG, "--- Decodifica el JSON ---");

                }
                catch (Exception ex) {
                    Log.e(TAG, "--- ERROR al decodificar el JSON ---");
                    Log.e(TAG, ex.toString());
                    tomaResponse = null;
                }

            } else {
                tomaResponse = null;
            }

        } catch (Exception e) {

            Log.e(TAG, "--- ERROR al consultar ---");
            Log.e(TAG, e.toString());
            tomaResponse = null;
        }

        return tomaResponse;

    }


}
