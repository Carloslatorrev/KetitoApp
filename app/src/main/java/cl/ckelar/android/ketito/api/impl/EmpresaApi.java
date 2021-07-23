package cl.ckelar.android.ketito.api.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cl.ckelar.android.ketito.api.IEmpresaApi;
import cl.ckelar.android.ketito.dto.api.EmpresaData;
import cl.ckelar.android.ketito.helpers.tags.TagApi;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class EmpresaApi {

    private static final String TAG = "EmpresaApi";

    /**
     * Obtiene el listado de empresas asociado al usuario
     *
     * @param contentType es la codificación de los parámetros
     * @param authorization contiene el dato bearer + " " + token
     *
     * @return List<EmpresaData>
     * **/
    public List<EmpresaData> getEmpresas(String contentType, String authorization) {

        List<EmpresaData> listEmpresas = new ArrayList<>();

        try {

            Log.i(TAG, "--- METHOD: getEmpresas ---");

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
            IEmpresaApi iEmpresaApi = retrofit.create(IEmpresaApi.class);

            // Define el valor por defecto del grant_type
            contentType = TagApi.API_HEADER_CONTENT_TYPE_VALUE;

            Call<ResponseBody> call = iEmpresaApi.getEmpresas(contentType, authorization);
            retrofit2.Response<ResponseBody> responseBody = call.execute();

            Log.i(TAG, call.request().url().toString());

            if (responseBody.isSuccessful()) {

                Log.i(TAG, "--- Obtiene las empresas asociadas al usuario ---");

                try {

                    String response = responseBody.body().string();

                    Log.d(TAG, response);   // Debug

                    Gson gson = new Gson();

                    Type collectionType = new TypeToken<List<EmpresaData>>(){}.getType();
                    listEmpresas = gson.fromJson(response, collectionType);

                    Log.i(TAG, "--- Decodifica el JSON ---");

                }
                catch (Exception ex) {
                    Log.e(TAG, "--- ERROR al decodificar el JSON Login ---");
                    Log.e(TAG, ex.toString());
                    listEmpresas = null;
                }

            } else {
                try {

                    String response = responseBody.errorBody().string();

                    Log.d(TAG, response);   // Debug

                    listEmpresas = null;

                }
                catch (Exception ex) {
                    Log.i(TAG, "--- ERROR al decodificar el JSON ---");
                    listEmpresas = null;
                    Log.e(TAG, ex.toString());
                }

                Log.e(TAG, "--- ERROR al consultar el Servicio ---");
                listEmpresas = null;

            }

        } catch (Exception e) {

            Log.e(TAG, "--- ERROR al consultar ---");
            Log.e(TAG, e.toString());
            listEmpresas = null;
        }

        return listEmpresas;

    }

    /**
     * Define con cual empresa trabajar
     *
     * @param contentType es la codificación de los parámetros
     * @param authorization contiene el dato bearer + " " + token
     *
     * @return true or false
     * **/
    public boolean useEmpresa(String contentType, String authorization, String idEmpresa) {

        boolean isUsed = false;

        try {

            Log.i(TAG, "--- METHOD: useEmpresa ---");

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
            IEmpresaApi iEmpresaApi = retrofit.create(IEmpresaApi.class);

            // Define el valor por defecto del grant_type
            contentType = TagApi.API_HEADER_CONTENT_TYPE_VALUE;

            Call<ResponseBody> call = iEmpresaApi.useEmpresa(contentType, authorization, idEmpresa);
            retrofit2.Response<ResponseBody> responseBody = call.execute();

            Log.i(TAG, call.request().url().toString());

            Log.i(TAG, "RESPONSEBODY: " + responseBody.toString());

            if (responseBody.isSuccessful()) {

                Log.i(TAG, "--- Obtiene las empresa seleccionada ---");
                Log.i(TAG, "Código HTTP: " + responseBody.code());

                //
                isUsed = (responseBody.code() == 200) ? true : false;

                Log.i(TAG, "USE: " + ((isUsed) ? "TRUE" : "FALSE"));

            } else {
                isUsed = false;
            }

        } catch (Exception e) {

            Log.e(TAG, "--- ERROR al consultar ---");
            Log.e(TAG, e.toString());
            isUsed = false;
        }

        return isUsed;

    }

}
