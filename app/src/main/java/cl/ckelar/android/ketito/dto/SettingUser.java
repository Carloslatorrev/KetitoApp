package cl.ckelar.android.ketito.dto;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Clase para almacenar la configuración del usuario.
 */
@Entity
public class SettingUser {
    @NonNull
    @PrimaryKey
    private int IdSetting;
    private Boolean EnvioSMS;
    private String Password;

    public int getIdSetting() {
        return IdSetting;
    }

    public void setIdSetting(int idSetting) {
        IdSetting = idSetting;
    }

    public Boolean getEnvioSMS() {
        return EnvioSMS;
    }

    public void setEnvioSMS(Boolean envioSMS) {
        EnvioSMS = envioSMS;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
