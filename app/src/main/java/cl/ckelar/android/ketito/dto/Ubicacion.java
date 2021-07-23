package cl.ckelar.android.ketito.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Clase ubicación
 */
@Entity
public class Ubicacion  {

    @NonNull
    @PrimaryKey
    private String IdUbicacion;
    private String Ubicacion;
    private String Descripcion;
    private int IdLocalidad;
    private String Localidad;
    private Boolean Selected;

    @NonNull
    public String getIdUbicacion() {
        return IdUbicacion;
    }

    public void setIdUbicacion(@NonNull String idUbicacion) {
        IdUbicacion = idUbicacion;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        Ubicacion = ubicacion;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getIdLocalidad() {
        return IdLocalidad;
    }

    public void setIdLocalidad(int idLocalidad) {
        IdLocalidad = idLocalidad;
    }

    public String getLocalidad() {
        return Localidad;
    }

    public void setLocalidad(String localidad) {
        Localidad = localidad;
    }

    public Boolean getSelected() {
        return Selected;
    }

    public void setSelected(Boolean selected) {
        Selected = selected;
    }
}
