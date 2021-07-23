package cl.ckelar.android.ketito.dto.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Clase para la serialización de la API Preguntas
 */
public class PreguntasData {

    @SerializedName("IdPregunta")
    @Expose
    private int idPregunta;
    @SerializedName("Pregunta")
    @Expose
    private String pregunta;
    @SerializedName("Subtitulo")
    @Expose
    private String subtitulo;
    @SerializedName("Indice")
    @Expose
    private int indice;
    @SerializedName("Obligatoria")
    @Expose
    private Boolean obligatoria;
    @SerializedName("EtiquetaMinimo")
    @Expose
    private String etiquetaMinimo;
    @SerializedName("EtiquetaMaximo")
    @Expose
    private String etiquetaMaximo;
    @SerializedName("RespuestaLarga")
    @Expose
    private Boolean respuestaLarga;
    @SerializedName("Escala")
    @Expose
    private int escala;
    @SerializedName("IdSimbolo")
    @Expose
    private int idSimbolo;
    @SerializedName("IdTipo")
    @Expose
    private int idTipo;
    @SerializedName("Tipo")
    @Expose
    private String tipo;
    @SerializedName("PreguntasAlternativas")
    @Expose
    private List<PreguntasAlternativasData> listPreguntasAlternativasData;

    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public Boolean getObligatoria() {
        return obligatoria;
    }

    public void setObligatoria(Boolean obligatoria) {
        this.obligatoria = obligatoria;
    }

    public String getEtiquetaMinimo() {
        return etiquetaMinimo;
    }

    public void setEtiquetaMinimo(String etiquetaMinimo) {
        this.etiquetaMinimo = etiquetaMinimo;
    }

    public String getEtiquetaMaximo() {
        return etiquetaMaximo;
    }

    public void setEtiquetaMaximo(String etiquetaMaximo) {
        this.etiquetaMaximo = etiquetaMaximo;
    }

    public Boolean getRespuestaLarga() {
        return respuestaLarga;
    }

    public void setRespuestaLarga(Boolean respuestaLarga) {
        this.respuestaLarga = respuestaLarga;
    }

    public int getEscala() {
        return escala;
    }

    public void setEscala(int escala) {
        this.escala = escala;
    }

    public int getIdSimbolo() {
        return idSimbolo;
    }

    public void setIdSimbolo(int idSimbolo) {
        this.idSimbolo = idSimbolo;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<PreguntasAlternativasData> getListPreguntasAlternativasData() {
        return listPreguntasAlternativasData;
    }

    public void setListPreguntasAlternativasData(List<PreguntasAlternativasData> listPreguntasAlternativasData) {
        this.listPreguntasAlternativasData = listPreguntasAlternativasData;
    }
}
