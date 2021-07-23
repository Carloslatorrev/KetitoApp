package cl.ckelar.android.ketito.dto.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Clase para la serialización de la API Respuestas
 */
public class RespuestaData {

    @SerializedName("Code")
    @Expose
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
