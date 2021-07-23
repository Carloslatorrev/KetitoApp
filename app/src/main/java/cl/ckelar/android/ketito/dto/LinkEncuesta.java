package cl.ckelar.android.ketito.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Clase linkEncuesta, para SMS
 */
@Entity
public class LinkEncuesta {

    @NonNull
    @PrimaryKey
    private String IdLinkEncuesta;
    private String IdEncuesta;
    private String LinkEncuesta;
    private String IdToma;

    @NonNull
    public String getIdLinkEncuesta() {
        return IdLinkEncuesta;
    }

    public void setIdLinkEncuesta(@NonNull String idLinkEncuesta) {
        IdLinkEncuesta = idLinkEncuesta;
    }

    public String getIdEncuesta() {
        return IdEncuesta;
    }

    public void setIdEncuesta(String idEncuesta) {
        IdEncuesta = idEncuesta;
    }

    public String getLinkEncuesta() {
        return LinkEncuesta;
    }

    public void setLinkEncuesta(String linkEncuesta) {
        LinkEncuesta = linkEncuesta;
    }

    public String getIdToma() {
        return IdToma;
    }

    public void setIdToma(String idToma) {
        IdToma = idToma;
    }
}
