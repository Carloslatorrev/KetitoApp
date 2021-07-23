package cl.ckelar.android.ketito.api.impl;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import cl.ckelar.android.ketito.api.ISingleSMSApi;
import cl.ckelar.android.ketito.dto.api.SingleSMSData;
import cl.ckelar.android.ketito.helpers.tags.TagApi;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class SingleSMSApi {

    private static final String TAG = "SingleSMSApi";
    /**
     * Crea una Toma de la encuesta
     *
     * @param contentType codificación de los parámetros
     * @param authorization contiene el código de autorización de la API
     * @param Destino telefono
     * @param Mensaje Mensaje
     * @param Programado Booleando si será programado o no
     *
     *
     * @return <TomaData> Obtiene la respuesta de la API
     * **/
    public SingleSMSData postEnvioSMS(String contentType, String authorization, String Destino, String Mensaje, String Programado, String FH_Programacion, String IdPlantilla) {

        SingleSMSData singleSMSData = new SingleSMSData();

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
            ISingleSMSApi iSingleSMSApi= retrofit.create(ISingleSMSApi.class);

            // Define el valor por defecto del grant_type
            contentType = TagApi.API_HEADER_CONTENT_TYPE_VALUE;

            Call<ResponseBody> call = iSingleSMSApi.postSingleSMS(contentType, authorization, Destino, Mensaje, Programado, FH_Programacion, IdPlantilla);
            retrofit2.Response<ResponseBody> responseBody = call.execute();

            Log.i(TAG, call.request().url().toString());

            Log.i(TAG, "RESPONSEBODY: " + responseBody.toString());

            if (responseBody.isSuccessful()) {

                Log.i(TAG, "--- Obtiene el resultado del envío ---");
                //Log.i(TAG, "Código HTTP: " + responseBody.code());

                try {

                    String response = String.valueOf(responseBody.code());

                    Log.d(TAG, response);   // Debug

                    //Gson gson = new Gson();

                    singleSMSData.setResponse(response);

                    Log.i(TAG, "--- Decodifica el JSON ---");

                }
                catch (Exception ex) {
                    Log.e(TAG, "--- ERROR al decodificar el JSON ---");
                    Log.e(TAG, ex.toString());
                    singleSMSData = null;
                }

            } else {
                singleSMSData = null;
            }

        } catch (Exception e) {

            Log.e(TAG, "--- ERROR al consultar ---");
            Log.e(TAG, e.toString());
            singleSMSData = null;
        }

        return singleSMSData;

    }
}
