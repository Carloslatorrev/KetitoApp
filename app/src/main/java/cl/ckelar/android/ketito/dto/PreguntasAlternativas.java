package cl.ckelar.android.ketito.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Clase Alternativas
 */
@Entity
public class PreguntasAlternativas {

    @NonNull
    @PrimaryKey
    private int IdAlternativa;
    private int IdPregunta;
    private String Alternativa;
    private int Indice;

    public int getIdAlternativa() {
        return IdAlternativa;
    }

    public void setIdAlternativa(int idAlternativa) {
        IdAlternativa = idAlternativa;
    }

    public int getIdPregunta() {
        return IdPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        IdPregunta = idPregunta;
    }

    public String getAlternativa() {
        return Alternativa;
    }

    public void setAlternativa(String alternativa) {
        Alternativa = alternativa;
    }

    public int getIndice() {
        return Indice;
    }

    public void setIndice(int indice) {
        Indice = indice;
    }


}
