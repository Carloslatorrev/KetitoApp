package cl.ckelar.android.ketito.dto.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Clase para la serialización de la API Destinatarios
 */
public class DestinatarioData {

    @SerializedName("IdDestino")
    @Expose
    private String idDestino;
    @SerializedName("Destino")
    @Expose
    private String destino;
    @SerializedName("Nombre")
    @Expose
    private String nombre;
    @SerializedName("Email")
    @Expose
    private String email;

    public String getIdDestino() {
        return idDestino;
    }

    public void setIdDestino(String idDestino) {
        this.idDestino = idDestino;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
