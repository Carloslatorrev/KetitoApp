package cl.ckelar.android.ketito.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Clase token de acceso para API
 */
@Entity
public class TokenC {

    @NonNull
    @PrimaryKey
    private String Token;
    private String TokenName;
    private Boolean Selected;
    private String FH_Token;

    @NonNull
    public String getToken() {
        return Token;
    }

    public void setToken(@NonNull String token) {
        Token = token;
    }

    public String getTokenName() {
        return TokenName;
    }

    public void setTokenName(String tokenName) {
        TokenName = tokenName;
    }

    public Boolean getSelected() {
        return Selected;
    }

    public void setSelected(Boolean selected) {
        Selected = selected;
    }

    public String getFH_Token() {
        return FH_Token;
    }

    public void setFH_Token(String FH_Token) {
        this.FH_Token = FH_Token;
    }
}
