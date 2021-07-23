package cl.ckelar.android.ketito.dto.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Clase para la serialización de la API RespuestasList
 */
public class RespuestasListData {

    @SerializedName("IdPregunta")
    @Expose
    private int IdPregunta;
    @SerializedName("IdAlternativa")
    @Expose
    private String IdAlternativa;
    @SerializedName("IdToma")
    @Expose
    private int IdToma;
    @SerializedName("Respuesta")
    @Expose
    private String Respuesta;
    @SerializedName("Nivel")
    @Expose
    private String Nivel;

    public int getIdPregunta() {
        return IdPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        IdPregunta = idPregunta;
    }

    public String getIdAlternativa() {
        return IdAlternativa;
    }

    public void setIdAlternativa(String idAlternativa) {
        IdAlternativa = idAlternativa;
    }

    public int getIdToma() {
        return IdToma;
    }

    public void setIdToma(int idToma) {
        IdToma = idToma;
    }

    public String getRespuesta() {
        return Respuesta;
    }

    public void setRespuesta(String respuesta) {
        Respuesta = respuesta;
    }

    public String getNivel() {
        return Nivel;
    }

    public void setNivel(String nivel) {
        Nivel = nivel;
    }
}
