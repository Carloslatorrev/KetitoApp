package cl.ckelar.android.ketito.api;

import cl.ckelar.android.ketito.helpers.tags.TagApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Interface para serializacion de parametros para la consulta a la API
 */
public interface ITokenApi {

    @GET(TagApi.API_URL_BASE + TagApi.API_PARAM_NAME_TOKEN_LIST)
    Call<ResponseBody> getToken(@Header(TagApi.API_HEADER_CONTENT_TYPE_NAME) String contentType,
                                @Header(TagApi.API_HEADER_AUTHORIZATION_NAME) String authorization);


}
