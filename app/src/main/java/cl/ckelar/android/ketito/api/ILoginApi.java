package cl.ckelar.android.ketito.api;

import cl.ckelar.android.ketito.helpers.tags.TagApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Interface para serializacion de parametros para la consulta a la API
 */
public interface ILoginApi {

    @POST(TagApi.API_URL_BASE + TagApi.API_URL_PATH_LOGIN)
    @FormUrlEncoded
    Call<ResponseBody> login(@Field(TagApi.API_PARAM_NAME_LOGIN_GRANTTYPE) String grant_type,
                             @Field(TagApi.API_PARAM_NAME_LOGIN_USERNAME) String userName,
                             @Field(TagApi.API_PARAM_NAME_LOGIN_PASSWORD) String password
    );

}
