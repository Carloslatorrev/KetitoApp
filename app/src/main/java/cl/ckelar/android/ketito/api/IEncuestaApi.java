package cl.ckelar.android.ketito.api;

import cl.ckelar.android.ketito.helpers.tags.TagApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Interface para serializacion de parametros para la consulta a la API
 */
public interface IEncuestaApi {

    //LIST ENCUESTA
    @GET(TagApi.API_URL_BASE + TagApi.API_URL_PATH_ENCUESTAS)
    Call<ResponseBody> getEncuestas(@Header(TagApi.API_HEADER_CONTENT_TYPE_NAME) String contentType,
                                  @Header(TagApi.API_HEADER_AUTHORIZATION_NAME) String authorization);

    //SELECT ENCUESTA
    @GET(TagApi.API_URL_BASE+TagApi.API_URL_PATH_LIST_ENCUESTAS_FIND)
    Call<ResponseBody> getFindEncuesta(@Header(TagApi.API_HEADER_CONTENT_TYPE_NAME) String contentType,
                                       @Header(TagApi.API_HEADER_AUTHORIZATION_NAME) String authorization,
                                       @Path("IdEncuesta") String idEncuesta);

    //GET LINK ENCUESTA
    @GET(TagApi.API_URL_BASE+TagApi.API_URL_PATH_GENERATE_LINK_ENCUESTAS)
    Call<ResponseBody> getLinkEncuesta(@Header(TagApi.API_HEADER_CONTENT_TYPE_NAME) String contentType,
                                       @Header(TagApi.API_HEADER_AUTHORIZATION_NAME) String authorization,
                                       @Path("IdEncuesta") String idEncuesta);


    //@POST

    //ENVIO ENCUESTA
    @POST(TagApi.API_URL_BASE + TagApi.API_URL_PATH_ENCUESTAS)
    @FormUrlEncoded
    Call<ResponseBody> sendEncuesta(@Header(TagApi.API_HEADER_CONTENT_TYPE_NAME) String contentType,
                                    @Header(TagApi.API_HEADER_AUTHORIZATION_NAME) String authorization,
                                    @Field(TagApi.API_PARAM_NAME_ENCUESTA_DESTINO_MOVIL) String destino,
                                    @Field(TagApi.API_PARAM_NAME_ENCUESTA_DESTINO_NOMBRE) String nombre,
                                    @Field(TagApi.API_PARAM_NAME_ENCUESTA_DESTINO_EMAIL) String email,
                                    @Field(TagApi.API_PARAM_NAME_ENCUESTA_DESTINO_IDENCUESTA) String idEncuesta);

}
