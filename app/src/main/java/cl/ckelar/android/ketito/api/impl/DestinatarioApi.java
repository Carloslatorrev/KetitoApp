package cl.ckelar.android.ketito.api.impl;

import android.util.Log;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import cl.ckelar.android.ketito.api.IDestinatarioApi;
import cl.ckelar.android.ketito.dto.api.DestinatarioData;
import cl.ckelar.android.ketito.helpers.tags.TagApi;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Clase para la consulta a la API Rest
 */
public class DestinatarioApi {

    private static final String TAG = "DestinatarioApi";


    /**
     * Obtiene los datos del destinatario por su N° de teléfono
     * **/
    public DestinatarioData findDestinatario(String contentType, String authorization, String movil) {

        DestinatarioData destinatarioData = new DestinatarioData();

        try {

            Log.i(TAG, "--- METHOD: findDestinatario ---");

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
            IDestinatarioApi iDestinatarioApi = retrofit.create(IDestinatarioApi.class);

            // Define el valor por defecto del grant_type
            contentType = TagApi.API_HEADER_CONTENT_TYPE_VALUE;

            Call<ResponseBody> call = iDestinatarioApi.findDestinatario(contentType, authorization, movil);
            retrofit2.Response<ResponseBody> responseBody = call.execute();

            Log.i(TAG, call.request().url().toString());

            if (responseBody.isSuccessful()) {

                Log.i(TAG, "--- Obtiene los datos del destinatario ---");
                Log.i(TAG, "RESPONSEBODY: " + responseBody.toString());

                try {

                    String response = responseBody.body().string();

                    Log.d(TAG, response);   // Debug

                    Gson gson = new Gson();
                    destinatarioData = gson.fromJson(response, DestinatarioData.class);

                    Log.i(TAG, "--- Decodifica el JSON ---");

                }
                catch (Exception ex) {
                    Log.e(TAG, "--- ERROR al decodificar el JSON ---");
                    Log.e(TAG, ex.toString());
                    destinatarioData = null;
                }

            } else {
                destinatarioData = null;
            }

        } catch (Exception e) {

            Log.e(TAG, "--- ERROR al consultar ---");
            Log.e(TAG, e.toString());
            destinatarioData = null;
        }

        return destinatarioData;

    }

}
