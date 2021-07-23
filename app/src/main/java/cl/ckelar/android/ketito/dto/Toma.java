package cl.ckelar.android.ketito.dto;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Clase Toma de le encuesta correspondiente
 */
@Entity
public class Toma {

    @NonNull
    @PrimaryKey
    private int IdToma;
    private String FH_Toma;
    private String IdEncuesta;
    private String FH_Termino;
    private Boolean Estado;
    private int IdTipoToma;
    private String IdArea;

    public int getIdToma() {
        return IdToma;
    }

    public void setIdToma(int idToma) {
        IdToma = idToma;
    }

    public String getFH_Toma() {
        return FH_Toma;
    }

    public void setFH_Toma(String FH_Toma) {
        this.FH_Toma = FH_Toma;
    }

    public String getIdEncuesta() {
        return IdEncuesta;
    }

    public void setIdEncuesta(String idEncuesta) {
        IdEncuesta = idEncuesta;
    }

    public String getFH_Termino() {
        return FH_Termino;
    }

    public void setFH_Termino(String FH_Termino) {
        this.FH_Termino = FH_Termino;
    }

    public Boolean getEstado() {
        return Estado;
    }

    public void setEstado(Boolean estado) {
        Estado = estado;
    }

    public int getIdTipoToma() {
        return IdTipoToma;
    }

    public void setIdTipoToma(int idTipoToma) {
        IdTipoToma = idTipoToma;
    }

    public String getIdArea() {
        return IdArea;
    }

    public void setIdArea(String idArea) {
        IdArea = idArea;
    }
}
