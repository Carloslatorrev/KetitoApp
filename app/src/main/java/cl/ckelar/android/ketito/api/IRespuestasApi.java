package cl.ckelar.android.ketito.api;


import java.util.List;

import cl.ckelar.android.ketito.dto.api.RespuestasListData;
import cl.ckelar.android.ketito.helpers.tags.TagApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Interface para serializacion de parametros para la consulta a la API
 */
public interface IRespuestasApi {


    //ENVIO Respuestas
    @POST(TagApi.API_URL_BASE + TagApi.API_URL_PATH_RESPUESTAS)
    //@FormUrlEncoded
    Call<ResponseBody> postRespuestas(@Header(TagApi.API_HEADER_CONTENT_TYPE_NAME) String contentType,
                                  @Header(TagApi.API_HEADER_AUTHORIZATION_NAME) String authorization,
                                  @Body List<RespuestasListData> respuestas
                                      /*    @Field(TagApi.API_PARAM_NAME_RESPUESTAS_IDTOMA) String idToma,
                                @Field(TagApi.API_PARAM_NAME_RESPUESTAS_IDPREGUNTA) String idPregunta,
                                @Field(TagApi.API_PARAM_NAME_RESPUESTAS_IDALTERNATIVA) String idAlternativa,
                                      @Field(TagApi.API_PARAM_NAME_RESPUESTAS_NIVEL) String Nivel,
                                      @Field(TagApi.API_PARAM_NAME_RESPUESTAS_RESPUESTA) String Respuesta */

    );
}
