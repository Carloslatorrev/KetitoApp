package cl.ckelar.android.ketito.api;

import cl.ckelar.android.ketito.helpers.tags.TagApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Interface para serializacion de parametros para la consulta a la API
 */
public interface IEmpresaApi {

    @GET(TagApi.API_URL_BASE + TagApi.API_URL_PATH_EMPRESAS)
    Call<ResponseBody> getEmpresas(@Header(TagApi.API_HEADER_CONTENT_TYPE_NAME) String contentType,
                                   @Header(TagApi.API_HEADER_AUTHORIZATION_NAME) String authorization);

    @POST(TagApi.API_URL_BASE + TagApi.API_URL_PATH_EMPRESAS_USE)
    Call<ResponseBody> useEmpresa(@Header(TagApi.API_HEADER_CONTENT_TYPE_NAME) String contentType,
                                  @Header(TagApi.API_HEADER_AUTHORIZATION_NAME) String authorization,
                                  @Path("idEmpresa") String idEmpresa);

}
