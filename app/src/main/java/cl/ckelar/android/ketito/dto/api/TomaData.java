package cl.ckelar.android.ketito.dto.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Clase para la serialización de la API Toma
 */
public class TomaData {

    @SerializedName("IdToma")
    @Expose
    private int idToma;
    @SerializedName("IdEncuesta")
    @Expose
    private String idEncuesta;
    @SerializedName("Encuesta")
    @Expose
    private String encuesta;
    @SerializedName("FH_toma")
    @Expose
    private String fh_toma;
    @SerializedName("FH_Termino")
    @Expose
    private String fh_termino;
    @SerializedName("Estado")
    @Expose
    private Boolean estado;
    @SerializedName("Ubicacion")
    @Expose
    private UbicacionData ubicacion;
    @SerializedName("IdTipoToma")
    @Expose
    private int idTipoToma;
    @SerializedName("TipoToma")
    @Expose
    private String tipoToma;


    public int getIdToma() {
        return idToma;
    }

    public void setIdToma(int idToma) {
        this.idToma = idToma;
    }

    public String getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(String idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public String getEncuesta() {
        return encuesta;
    }

    public void setEncuesta(String encuesta) {
        this.encuesta = encuesta;
    }

    public String getFh_toma() {
        return fh_toma;
    }

    public void setFh_toma(String fh_toma) {
        this.fh_toma = fh_toma;
    }

    public String getFh_termino() {
        return fh_termino;
    }

    public void setFh_termino(String fh_termino) {
        this.fh_termino = fh_termino;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public UbicacionData getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(UbicacionData ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getIdTipoToma() {
        return idTipoToma;
    }

    public void setIdTipoToma(int idTipoToma) {
        this.idTipoToma = idTipoToma;
    }

    public String getTipoToma() {
        return tipoToma;
    }

    public void setTipoToma(String tipoToma) {
        this.tipoToma = tipoToma;
    }
}
