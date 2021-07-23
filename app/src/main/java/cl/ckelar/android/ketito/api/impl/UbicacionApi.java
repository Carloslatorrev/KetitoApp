package cl.ckelar.android.ketito.api.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cl.ckelar.android.ketito.api.IUbicacionApi;
import cl.ckelar.android.ketito.dto.api.UbicacionData;
import cl.ckelar.android.ketito.helpers.tags.TagApi;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class UbicacionApi {

    private static final String TAG = "UbicacionApi";

    /**
     * Obtiene el listado de encuestas de la empresa
     *
     * @param contentType es la codificación de los parámetros
     * @param authorization contiene el dato bearer + " " + token
     *
     * @return List<EncuestaData>
     * **/
    public List<UbicacionData> getUbicaciones(String contentType, String authorization) {

        List<UbicacionData> listUbicacion = new ArrayList<>();

        try {

            Log.i(TAG, "--- METHOD: getUbicacion ---");

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
            IUbicacionApi iUbicacionApi = retrofit.create(IUbicacionApi.class);

            // Define el valor por defecto del grant_type
            contentType = TagApi.API_HEADER_CONTENT_TYPE_VALUE;

            Call<ResponseBody> call = iUbicacionApi.getUbicacion(contentType, authorization);
            retrofit2.Response<ResponseBody> responseBody = call.execute();

            Log.i(TAG, call.request().url().toString());

            if (responseBody.isSuccessful()) {

                Log.i(TAG, "--- Obtiene las ubicaciones asociadas a la empresa ---");
                Log.i(TAG, "RESPONSEBODY: " + responseBody.toString());

                try {

                    String response = responseBody.body().string();

                    Log.d(TAG, response);   // Debug

                    Gson gson = new Gson();

                    Type collectionType = new TypeToken<List<UbicacionData>>(){}.getType();
                    listUbicacion = gson.fromJson(response, collectionType);

                    Log.i(TAG, "--- Decodifica el JSON ---");

                }
                catch (Exception ex) {
                    Log.e(TAG, "--- ERROR al decodificar el JSON ---");
                    Log.e(TAG, ex.toString());
                    listUbicacion = null;
                }

            } else {

                try {

                    String response = responseBody.errorBody().string();
                    Log.d(TAG, response);   // Debug

                    listUbicacion = null;

                }
                catch (Exception ex) {
                    Log.i(TAG, "--- ERROR al decodificar el JSON ---");
                    listUbicacion = null;
                    Log.e(TAG, ex.toString());
                }

                Log.e(TAG, "--- ERROR al consultar el Servicio ---");
                listUbicacion = null;

            }

        } catch (Exception e) {

            Log.e(TAG, "--- ERROR al consultar ---");
            Log.e(TAG, e.toString());
            listUbicacion = null;
        }

        return listUbicacion;

    }

    public UbicacionData getFindEncuesta (String contentType, String authorization, String idUbicacion){

        UbicacionData ubicacionData = new UbicacionData();

        try {

            Log.i(TAG, "--- METHOD: getFindUbicacion ---");

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
            IUbicacionApi iUbicacionApi = retrofit.create(IUbicacionApi.class);

            // Define el valor por defecto del grant_type
            contentType = TagApi.API_HEADER_CONTENT_TYPE_VALUE;


            Call<ResponseBody> call = iUbicacionApi.getFindUbicacion(contentType, authorization, idUbicacion);
            retrofit2.Response<ResponseBody> responseBody = call.execute();

            Log.i(TAG, call.request().url().toString());

            if (responseBody.isSuccessful()) {

                Log.i(TAG, "--- Obtiene la Ubicación asociada al Id ---");
                Log.i(TAG, "RESPONSEBODY: " + responseBody.toString());

                try {

                    String response = responseBody.body().string();

                    Log.d(TAG, response);   // Debug

                    Gson gson = new Gson();

                    ubicacionData = gson.fromJson(response, UbicacionData.class);

                    Log.i(TAG, "--- Decodifica el JSON ---");

                }
                catch (Exception ex) {
                    Log.e(TAG, "--- ERROR al decodificar el JSON ---");
                    Log.e(TAG, ex.toString());
                    ubicacionData = null;
                }

            } else {

                try {

                    String response = responseBody.errorBody().string();

                    Log.d(TAG, response);   // Debug

                    ubicacionData = null;

                }
                catch (Exception ex) {
                    Log.i(TAG, "--- ERROR al decodificar el JSON ---");
                    ubicacionData = null;
                    Log.e(TAG, ex.toString());
                }

                Log.e(TAG, "--- ERROR al consultar el Servicio ---");
                ubicacionData = null;

            }

        } catch (Exception e) {

            Log.e(TAG, "--- ERROR al consultar ---");
            Log.e(TAG, e.toString());
            ubicacionData = null;
        }

        return ubicacionData;

    }


}
