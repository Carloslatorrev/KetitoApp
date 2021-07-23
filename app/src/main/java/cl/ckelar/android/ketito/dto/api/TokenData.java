package cl.ckelar.android.ketito.dto.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Clase para la serialización de la API Token
 */
public class TokenData {

    @SerializedName("TokenName")
    @Expose
    private String tokenName;
    @SerializedName("Token")
    @Expose
    private String token;
    @SerializedName("FH_Token")
    @Expose
    private String fh_token;

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFh_token() {
        return fh_token;
    }

    public void setFh_token(String fh_token) {
        this.fh_token = fh_token;
    }
}
