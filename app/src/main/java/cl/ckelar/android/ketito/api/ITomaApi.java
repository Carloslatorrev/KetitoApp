package cl.ckelar.android.ketito.api;

import cl.ckelar.android.ketito.helpers.tags.TagApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Interface para serializacion de parametros para la consulta a la API
 */
public interface ITomaApi {

    //ENVIO ENCUESTA
    @POST(TagApi.API_URL_BASE + TagApi.API_URL_PATH_TOMA)
    @FormUrlEncoded
    Call<ResponseBody> postToma(@Header(TagApi.API_HEADER_CONTENT_TYPE_NAME) String contentType,
                                    @Header(TagApi.API_HEADER_AUTHORIZATION_NAME) String authorization,
                                    @Field(TagApi.API_PARAM_NAME_TOMA_IDENCUESTA) String idEncuesta,
                                    @Field(TagApi.API_PARAM_NAME_TOMA_IDUBICACION) String idUbicacion
                                    );

}
