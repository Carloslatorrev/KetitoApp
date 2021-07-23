package cl.ckelar.android.ketito.dto.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Clase para la serialización de la API Ubicacion
 */
public class UbicacionData {

    @SerializedName("IdUbicacion")
    @Expose
    private String idUbicacion;
    @SerializedName("Ubicacion")
    @Expose
    private String ubicacion;
    @SerializedName("Descripcion")
    @Expose
    private String descripcion;
    @SerializedName("IdLocalidad")
    @Expose
    private int idLocalidad;
    @SerializedName("Localidad")
    @Expose
    private String localidad;


    public String getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(String idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad(int idLocalidad) {
        this.idLocalidad = idLocalidad;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }
}
