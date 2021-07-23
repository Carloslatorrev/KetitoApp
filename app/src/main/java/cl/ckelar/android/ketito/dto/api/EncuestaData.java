package cl.ckelar.android.ketito.dto.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Clase para la serialización de la API Encuesta
 */
public class EncuestaData {

    @SerializedName("IdEncuesta")
    @Expose
    private String idEncuesta;
    @SerializedName("Titulo")
    @Expose
    private String titulo;
    @SerializedName("Descripcion")
    @Expose
    private String descripcion;
    @SerializedName("Tipo")
    @Expose
    private Integer tipo;
    @SerializedName("FH_Creation")
    @Expose
    private String fh_Creation;
    @SerializedName("UsuarioCreacion")
    @Expose
    private String usuarioCreacion;
    @SerializedName("Preguntas")
    @Expose
    private List<PreguntasData> preguntasData;
    @SerializedName("BannerBackgroundColor")
    @Expose
    private String bannerBackgroundColor;
    @SerializedName("TitleFontColor")
    @Expose
    private String titleFontColor;
    @SerializedName("SubTitleFontColor")
    @Expose
    private String subtitleFontColor;
    @SerializedName("BackgroundImageUrl")
    @Expose
    private String backgroundImageUrl;




    public String getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(String idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getFh_Creation() {
        return fh_Creation;
    }

    public void setFh_Creation(String fh_Creation) {
        this.fh_Creation = fh_Creation;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public List<PreguntasData> getPreguntasData() {
        return preguntasData;
    }

    public void setPreguntasData(List<PreguntasData> preguntasData) {
        this.preguntasData = preguntasData;
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
