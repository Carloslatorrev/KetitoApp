package cl.ckelar.android.ketito.dto.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Clase para la serialización de la API Post response de la encuesta
 */
public class EncuestaPostResponse {

    @SerializedName("IdEncuesta")
    @Expose
    private String idEncuesta;
    @SerializedName("EmailResult")
    @Expose
    private String emailResult;
    @SerializedName("SMSResult")
    @Expose
    private String sMSResult;
    @SerializedName("Elapsed")
    @Expose
    private Double elapsed;

    public String getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(String idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public String getEmailResult() {
        return emailResult;
    }

    public void setEmailResult(String emailResult) {
        this.emailResult = emailResult;
    }

    public String getSMSResult() {
        return sMSResult;
    }

    public void setSMSResult(String sMSResult) {
        this.sMSResult = sMSResult;
    }

    public Double getElapsed() {
        return elapsed;
    }

    public void setElapsed(Double elapsed) {
        this.elapsed = elapsed;
    }

}
