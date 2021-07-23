package cl.ckelar.android.ketito.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;


/**
 * Clase que almacena los datos de conexión del Usuario, como credenciales y token de acceso.
 */
@Entity
public class UserSesion {

    @NonNull
    @PrimaryKey
    private String access_token;
    private String token_type;
    private long expires_in;
    private String userName;
    private String issued;
    private String expires;

    @NonNull
    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(@NonNull String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }
}
