package cl.ckelar.android.ketito.dto;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Clase para Respuestas
 */
@Entity
public class Respuesta {

    @NonNull
    @PrimaryKey
    private String IdRespuesta;
    private int IdPregunta;
    private String idAlternativa;
    private String Respuesta;
    private String Nivel;
    private int IdToma;

    public String getIdRespuesta() {
        return IdRespuesta;
    }

    public void setIdRespuesta(String idRespuesta) {
        IdRespuesta = idRespuesta;
    }

    public int getIdPregunta() {
        return IdPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        IdPregunta = idPregunta;
    }

    public String getIdAlternativa() {
        return idAlternativa;
    }

    public void setIdAlternativa(String idAlternativa) {
        this.idAlternativa = idAlternativa;
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

    public int getIdToma() {
        return IdToma;
    }

    public void setIdToma(int idToma) {
        IdToma = idToma;
    }
}
