package cl.ckelar.android.ketito.dto;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Clase para parametros de SMS
 */
@Entity
public class SingleSMS {

    @PrimaryKey
    @NonNull
    private String IdSingleSMS;
    private String Destino;
    private String Mensaje;
    private Boolean Programacion;
    private String FH_programacion;
    private String idPlantilla;

    @NonNull
    public String getIdSingleSMS() {
        return IdSingleSMS;
    }

    public void setIdSingleSMS(@NonNull String idSingleSMS) {
        IdSingleSMS = idSingleSMS;
    }

    public String getDestino() {
        return Destino;
    }

    public void setDestino(String destino) {
        Destino = destino;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }

    public Boolean getProgramacion() {
        return Programacion;
    }

    public void setProgramacion(Boolean programacion) {
        Programacion = programacion;
    }

    public String getFH_programacion() {
        return FH_programacion;
    }

    public void setFH_programacion(String FH_programacion) {
        this.FH_programacion = FH_programacion;
    }

    public String getIdPlantilla() {
        return idPlantilla;
    }

    public void setIdPlantilla(String idPlantilla) {
        this.idPlantilla = idPlantilla;
    }
}
