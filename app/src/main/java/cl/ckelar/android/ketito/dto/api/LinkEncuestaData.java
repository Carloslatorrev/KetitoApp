package cl.ckelar.android.ketito.dto.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Clase para la serialización de la API link de la encuesta browser
 */
public class LinkEncuestaData {

    @SerializedName("Link")
    @Expose
    private String linkEncuesta;
    @SerializedName("idToma")
    @Expose
    private String idToma;

    public String getLinkEncuesta() {
        return linkEncuesta;
    }

    public void setLinkEncuesta(String linkEncuesta) {
        this.linkEncuesta = linkEncuesta;
    }

    public String getIdToma() {
        return idToma;
    }

    public void setIdToma(String idToma) {
        this.idToma = idToma;
    }
}
