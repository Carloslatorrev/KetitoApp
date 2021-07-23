package cl.ckelar.android.ketito.dto;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Clase Encuesta
 */
@Entity
public class Encuesta {

    @NonNull
    @PrimaryKey
    private String IdEncuesta;
    private String Titulo;
    private String Descripcion;
    private Integer IdTipo;
    private String FH_creation;
    private String UsuarioCreacion;
    private Boolean Selected;
    private String bannerBackgroundColor;
    private String titleFontColor;
    private String subtitleFontColor;
    private String backgroundImageUrl;


    @NonNull
    public String getIdEncuesta() {
        return IdEncuesta;
    }

    public void setIdEncuesta(@NonNull String idEncuesta) {
        IdEncuesta = idEncuesta;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public Integer getIdTipo() {
        return IdTipo;
    }

    public void setIdTipo(Integer idTipo) {
        IdTipo = idTipo;
    }

    public String getFH_creation() {
        return FH_creation;
    }

    public void setFH_creation(String FH_creation) {
        this.FH_creation = FH_creation;
    }

    public String getUsuarioCreacion() {
        return UsuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        UsuarioCreacion = usuarioCreacion;
    }

    public Boolean getSelected() {
        return Selected;
    }

    public void setSelected(Boolean selected) {
        Selected = selected;
    }

    public String getBannerBackgroundColor() {
        return bannerBackgroundColor;
    }

    public void setBannerBackgroundColor(String bannerBackgroundColor) {
        this.bannerBackgroundColor = bannerBackgroundColor;
    }

    public String getTitleFontColor() {
        return titleFontColor;
    }

    public void setTitleFontColor(String titleFontColor) {
        this.titleFontColor = titleFontColor;
    }

    public String getSubtitleFontColor() {
        return subtitleFontColor;
    }

    public void setSubtitleFontColor(String subtitleFontColor) {
        this.subtitleFontColor = subtitleFontColor;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }
}
