package cl.ckelar.android.ketito.dto.api;

/**
 * Clase para la serialización de la API Login Response
 */
public class LoginResponse {

    private Login login;
    private LoginError loginError;

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public LoginError getLoginError() {
        return loginError;
    }

    public void setLoginError(LoginError loginError) {
        this.loginError = loginError;
    }
}
