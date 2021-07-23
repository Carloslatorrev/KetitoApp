package cl.ckelar.android.ketito.dto;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Clase Preguntas
 */
@Entity
public class Preguntas {

    @NonNull
    @PrimaryKey
    private int IdPregunta;
    private String IdEncuesta;
    private int IdTipo;
    private String Pregunta;
    private String Subtitulo;
    private int Indice;
    private Boolean Obligatoria;
    private int IdSimbolo;
    private String EtiquetaMinimo;
    private String EtiquetaMaximo;
    private Boolean RespuestaLarga;
    private String NomTipo;
    private int Escala;


    public int getIdPregunta() {
        return IdPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        IdPregunta = idPregunta;
    }

    public String getIdEncuesta() {
        return IdEncuesta;
    }

    public void setIdEncuesta(String idEncuesta) {
        IdEncuesta = idEncuesta;
    }

    public int getIdTipo() {
        return IdTipo;
    }

    public void setIdTipo(int idTipo) {
        IdTipo = idTipo;
    }

    public String getPregunta() {
        return Pregunta;
    }

    public void setPregunta(String pregunta) {
        Pregunta = pregunta;
    }

    public String getSubtitulo() {
        return Subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        Subtitulo = subtitulo;
    }

    public int getIndice() {
        return Indice;
    }

    public void setIndice(int indice) {
        Indice = indice;
    }

    public Boolean getObligatoria() {
        return Obligatoria;
    }

    public void setObligatoria(Boolean obligatoria) {
        Obligatoria = obligatoria;
    }

    public int getIdSimbolo() {
        return IdSimbolo;
    }

    public void setIdSimbolo(int idSimbolo) {
        IdSimbolo = idSimbolo;
    }

    public String getEtiquetaMinimo() {
        return EtiquetaMinimo;
    }

    public void setEtiquetaMinimo(String etiquetaMinimo) {
        EtiquetaMinimo = etiquetaMinimo;
    }

    public String getEtiquetaMaximo() {
        return EtiquetaMaximo;
    }

    public void setEtiquetaMaximo(String etiquetaMaximo) {
        EtiquetaMaximo = etiquetaMaximo;
    }

    public Boolean getRespuestaLarga() {
        return RespuestaLarga;
    }

    public void setRespuestaLarga(Boolean respuestaLarga) {
        RespuestaLarga = respuestaLarga;
    }

    public String getNomTipo() {
        return NomTipo;
    }

    public void setNomTipo(String nomTipo) {
        NomTipo = nomTipo;
    }

    public int getEscala() {
        return Escala;
    }

    public void setEscala(int escala) {
        Escala = escala;
    }

    @Override
    public String toString() {
        return Pregunta;
    }
}
