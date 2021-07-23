package cl.ckelar.android.ketito.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Clase Empresa
 */
@Entity
public class Empresa {

    @NonNull
    @PrimaryKey
    private String IdEmpresa;
    private String Razon;
    private String Rut;
    private String Telefono;
    private String Web;
    private String Correo;
    private String ContactoEmpresa;
    private String DataLetters;
    private String RutaImagen;
    private boolean Selected;

    @NonNull
    public String getIdEmpresa() {
        return IdEmpresa;
    }

    public void setIdEmpresa(@NonNull String idEmpresa) {
        IdEmpresa = idEmpresa;
    }

    public String getRazon() {
        return Razon;
    }

    public void setRazon(String razon) {
        Razon = razon;
    }

    public String getRut() {
        return Rut;
    }

    public void setRut(String rut) {
        Rut = rut;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getWeb() {
        return Web;
    }

    public void setWeb(String web) {
        Web = web;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getContactoEmpresa() {
        return ContactoEmpresa;
    }

    public void setContactoEmpresa(String contactoEmpresa) {
        ContactoEmpresa = contactoEmpresa;
    }

    public String getDataLetters() {
        return DataLetters;
    }

    public void setDataLetters(String dataLetters) {
        DataLetters = dataLetters;
    }

    public boolean isSelected() {
        return Selected;
    }

    public void setSelected(boolean selected) {
        Selected = selected;
    }

    public String getRutaImagen() {
        return RutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        RutaImagen = rutaImagen;
    }
}
