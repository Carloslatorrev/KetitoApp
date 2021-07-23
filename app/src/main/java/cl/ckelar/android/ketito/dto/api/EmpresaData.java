package cl.ckelar.android.ketito.dto.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Clase para la serialización de la API Empresa
 */
public class EmpresaData {

    @SerializedName("IdEmpresa")
    @Expose
    private String idEmpresa;
    @SerializedName("Razon")
    @Expose
    private String razon;
    @SerializedName("Rut")
    @Expose
    private String rut;
    @SerializedName("Telefono")
    @Expose
    private String telefono;
    @SerializedName("Web")
    @Expose
    private String web;
    @SerializedName("Correo")
    @Expose
    private String correo;
    @SerializedName("ContactoEmpresa")
    @Expose
    private String contactoEmpresa;
    @SerializedName("DataLetters")
    @Expose
    private String dataLetters;
    @SerializedName("RutaImagen")
    @Expose
    private String rutaImagen;

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContactoEmpresa() {
        return contactoEmpresa;
    }

    public void setContactoEmpresa(String contactoEmpresa) {
        this.contactoEmpresa = contactoEmpresa;
    }

    public String getDataLetters() {
        return dataLetters;
    }

    public void setDataLetters(String dataLetters) {
        this.dataLetters = dataLetters;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
}
