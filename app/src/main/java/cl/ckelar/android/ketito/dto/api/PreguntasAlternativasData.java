package cl.ckelar.android.ketito.dto.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Clase para la serialización de la API Alternativas
 */
public class PreguntasAlternativasData {

    @SerializedName("IdAlternativa")
    @Expose
    private int idAlternativa;
    @SerializedName("Alternativa")
    @Expose
    private String alternativa;
    @SerializedName("Indice")
    @Expose
    private int indice;

    public int getIdAlternativa() {
        return idAlternativa;
    }

    public void setIdAlternativa(int idAlternativa) {
        this.idAlternativa = idAlternativa;
    }

    public String getAlternativa() {
        return alternativa;
    }

    public void setAlternativa(String alternativa) {
        this.alternativa = alternativa;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }
}
