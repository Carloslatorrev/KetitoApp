package cl.ckelar.android.ketito.api.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cl.ckelar.android.ketito.api.IEncuestaApi;
import cl.ckelar.android.ketito.dto.api.EncuestaData;
import cl.ckelar.android.ketito.dto.api.EncuestaPostResponse;
import cl.ckelar.android.ketito.dto.api.LinkEncuestaData;
import cl.ckelar.android.ketito.dto.api.PreguntasData;
import cl.ckelar.android.ketito.helpers.tags.TagApi;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class EncuestaApi {

    private static final String TAG = "EncuestaApi";

    /**
     * Obtiene el listado de encuestas de la empresa
     *
     * @param contentType es la codificación de los parámetros
     * @param authorization contiene el dato bearer + " " + token
     *
     * @return List<EncuestaData>
     * **/
    public List<EncuestaData> getEncuestas(String contentType, String authorization) {

        List<EncuestaData> listEncuestas = new ArrayList<>();

        try {

            Log.i(TAG, "--- METHOD: getEncuestas ---");

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
            IEncuestaApi iEncuestaApi = retrofit.create(IEncuestaApi.class);

            // Define el valor por defecto del grant_type
            contentType = TagApi.API_HEADER_CONTENT_TYPE_VALUE;

            Call<ResponseBody> call = iEncuestaApi.getEncuestas(contentType, authorization);
            retrofit2.Response<ResponseBody> responseBody = call.execute();

            Log.i(TAG, call.request().url().toString());

            if (responseBody.isSuccessful()) {

                Log.i(TAG, "--- Obtiene las encuestas asociadas a la empresa ---");
                Log.i(TAG, "RESPONSEBODY: " + responseBody.toString());

                try {

                    String response = responseBody.body().string();

                    Log.d(TAG, response);   // Debug

                    Gson gson = new Gson();

                    Type collectionType = new TypeToken<List<EncuestaData>>(){}.getType();
                    listEncuestas = gson.fromJson(response, collectionType);

                    Log.i(TAG, "--- Decodifica el JSON ---");

                }
                catch (Exception ex) {
                    Log.e(TAG, "--- ERROR al decodificar el JSON ---");
                    Log.e(TAG, ex.toString());
                    listEncuestas = null;
                }

            } else {

                try {

                    String response = responseBody.errorBody().string();

                    Log.d(TAG, response);   // Debug

                    listEncuestas = null;

                }
                catch (Exception ex) {
                    Log.i(TAG, "--- ERROR al decodificar el JSON ---");
                    listEncuestas = null;
                    Log.e(TAG, ex.toString());
                }

                Log.e(TAG, "--- ERROR al consultar el Servicio ---");
                listEncuestas = null;

            }

        } catch (Exception e) {

            Log.e(TAG, "--- ERROR al consultar ---");
            Log.e(TAG, e.toString());
            listEncuestas = null;
        }

        return listEncuestas;

    }

    /**
     * Obtiene la encuesta seleccionada de la empresa
     *
     * @param contentType es la codificación de los parámetros
     * @param authorization contiene el dato bearer + " " + token
     * @param idEncuesta contiene el dato de id de la encuesta
     *
     * @return EncuestaData
     * **/

    public List<PreguntasData> getFindEncuesta (String contentType, String authorization, String idEncuesta){
        EncuestaData encuestaData = new EncuestaData();
        List<PreguntasData> listPreguntasData;


        try {

            Log.i(TAG, "--- METHOD: getFindEncuesta ---");

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
            IEncuestaApi iEncuestaApi = retrofit.create(IEncuestaApi.class);

            // Define el valor por defecto del grant_type
            contentType = TagApi.API_HEADER_CONTENT_TYPE_VALUE;


            Call<ResponseBody> call = iEncuestaApi.getFindEncuesta(contentType, authorization, idEncuesta);
            retrofit2.Response<ResponseBody> responseBody = call.execute();

            Log.i(TAG, call.request().url().toString());

            if (responseBody.isSuccessful()) {

                Log.i(TAG, "--- Obtiene la encuesta asociada al Id ---");
                Log.i(TAG, "RESPONSEBODY: " + responseBody.toString());

                try {

                    String response = responseBody.body().string();

                    Log.d(TAG, response);   // Debug

                    Gson gson = new Gson();

                    encuestaData = gson.fromJson(response, EncuestaData.class);
                    listPreguntasData = encuestaData.getPreguntasData();

                    Log.i(TAG, "--- Decodifica el JSON ---");

                }
                catch (Exception ex) {
                    Log.e(TAG, "--- ERROR al decodificar el JSON ---");
                    Log.e(TAG, ex.toString());
                    listPreguntasData = null;
                }

            } else {

                try {

                    String response = responseBody.errorBody().string();

                    Log.d(TAG, response);   // Debug

                    listPreguntasData = null;

                }
                catch (Exception ex) {
                    Log.i(TAG, "--- ERROR al decodificar el JSON ---");
                    listPreguntasData = null;
                    Log.e(TAG, ex.toString());
                }

                Log.e(TAG, "--- ERROR al consultar el Servicio ---");
                listPreguntasData = null;

            }

        } catch (Exception e) {

            Log.e(TAG, "--- ERROR al consultar ---");
            Log.e(TAG, e.toString());
            listPreguntasData = null;
        }

        return listPreguntasData;

    }

    /**
     * Obtiene el link de la encuesta seleccionada de la empresa
     *
     * @param contentType es la codificación de los parámetros
     * @param authorization contiene el dato bearer + " " + token
     * @param idEncuesta contiene el dato de id de la encuesta
     *
     * @return linkEncuesta
     * **/

    public LinkEncuestaData getLinkEncuesta (String contentType, String authorization, String idEncuesta){
        LinkEncuestaData linkEncuestaData = new LinkEncuestaData();

        try {

            Log.i(TAG, "--- METHOD: getFindEncuesta ---");

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
            IEncuestaApi iEncuestaApi = retrofit.create(IEncuestaApi.class);

            // Define el valor por defecto del grant_type
            contentType = TagApi.API_HEADER_CONTENT_TYPE_VALUE;


            Call<ResponseBody> call = iEncuestaApi.getLinkEncuesta(contentType, authorization, idEncuesta);
            retrofit2.Response<ResponseBody> responseBody = call.execute();

            Log.i(TAG, call.request().url().toString());

            if (responseBody.isSuccessful()) {

                Log.i(TAG, "--- Obtiene el link asociada a la encuesta ---");
                Log.i(TAG, "RESPONSEBODY: " + responseBody.toString());

                try {

                    String response = responseBody.body().string();

                    Log.d(TAG, response);   // Debug

                    Gson gson = new Gson();

                    linkEncuestaData = gson.fromJson(response, LinkEncuestaData.class);

                    Log.i(TAG, "--- Decodifica el JSON ---");

                }
                catch (Exception ex) {
                    Log.e(TAG, "--- ERROR al decodificar el JSON ---");
                    Log.e(TAG, ex.toString());
                    linkEncuestaData = null;
                }

            } else {

                try {

                    String response = responseBody.errorBody().string();

                    Log.d(TAG, response);   // Debug

                    linkEncuestaData = null;

                }
                catch (Exception ex) {
                    Log.i(TAG, "--- ERROR al decodificar el JSON ---");
                    linkEncuestaData = null;
                    Log.e(TAG, ex.toString());
                }

                Log.e(TAG, "--- ERROR al consultar el Servicio ---");
                linkEncuestaData = null;

            }

        } catch (Exception e) {

            Log.e(TAG, "--- ERROR al consultar ---");
            Log.e(TAG, e.toString());
            linkEncuestaData = null;
        }

        return linkEncuestaData;

    }


    /**
     * Envía la encuesta al cliente
     *
     * @param contentType codificación de los parámetros
     * @param authorization contiene el código de autorización de la API
     * @param destino N° de teléfono del destinatario
     * @param nombre Nombre del destinatario
     * @param email Email del destinatario
     * @param idEncuesta ID de la Encuesta
     *
     * @return <EncuestaPostResponse> Obtiene la respuesta de la API
     * **/
    public EncuestaPostResponse sendEncuesta(String contentType, String authorization, String destino, String nombre, String email, String idEncuesta) {

        EncuestaPostResponse postResponse = new EncuestaPostResponse();

        try {

            Log.i(TAG, "--- METHOD: sendEncuesta ---");

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
            IEncuestaApi iEncuestaApi = retrofit.create(IEncuestaApi.class);

            // Define el valor por defecto del grant_type
            contentType = TagApi.API_HEADER_CONTENT_TYPE_VALUE;

            Call<ResponseBody> call = iEncuestaApi.sendEncuesta(contentType, authorization, destino, nombre, email, idEncuesta);
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

                    postResponse = gson.fromJson(response, EncuestaPostResponse.class);

                    Log.i(TAG, "--- Decodifica el JSON ---");

                }
                catch (Exception ex) {
                    Log.e(TAG, "--- ERROR al decodificar el JSON ---");
                    Log.e(TAG, ex.toString());
                    postResponse = null;
                }

            } else {
                postResponse = null;
            }

        } catch (Exception e) {

            Log.e(TAG, "--- ERROR al consultar ---");
            Log.e(TAG, e.toString());
            postResponse = null;
        }

        return postResponse;

    }





}
