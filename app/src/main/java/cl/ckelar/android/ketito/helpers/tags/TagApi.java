package cl.ckelar.android.ketito.helpers.tags;

/**
 * Clase que almacena los Tag o parametros para la conexión a la API
 */
public class TagApi {


    public static final String API_URL_BASE = "http://45.7.229.97:8003";
    public static final String API_URL_PWD_FORGOT = "http://test.ketito.cl/Account/ForgotPassword";

    // LOGIN
    public static final String API_URL_PATH_LOGIN = "/token";
    public static final String API_PARAM_NAME_LOGIN_GRANTTYPE = "grant_type";
    public static final String API_PARAM_NAME_LOGIN_USERNAME = "userName";
    public static final String API_PARAM_NAME_LOGIN_PASSWORD = "password";
    public static final String API_PARAM_VALUE_LOGIN_GRANTTYPE = "password";

    // TOKEN
    public static final String API_PARAM_NAME_TOKEN_LIST = "/Empresas/Token/List";

    // EMPRESA
    public static final String API_URL_PATH_EMPRESAS = "/Empresas/List";
    public static final String API_URL_PATH_EMPRESAS_USE = "/Empresas/USE/{idEmpresa}";

    // ENCUESTA
    public static final String API_URL_PATH_ENCUESTAS = "/Encuestas/List";
    public static final String API_URL_PATH_LIST_ENCUESTAS_FIND="/Encuestas/Find/{IdEncuesta}";
    public static final String API_URL_PATH_GENERATE_LINK_ENCUESTAS="/Encuestas/Link/Create/{IdEncuesta}";
    public static final String API_PARAM_NAME_ENCUESTA_DESTINO_MOVIL = "Destino";
    public static final String API_PARAM_NAME_ENCUESTA_DESTINO_NOMBRE = "Nombre";
    public static final String API_PARAM_NAME_ENCUESTA_DESTINO_EMAIL = "Email";
    public static final String API_PARAM_NAME_ENCUESTA_DESTINO_IDENCUESTA = "IdEncuesta";

    //TOMA
    public static final String API_URL_PATH_TOMA= "/Encuestas/Tomas/Create";
    public static final String API_PARAM_NAME_TOMA_IDENCUESTA="IdEncuesta";
    public static final String API_PARAM_NAME_TOMA_IDUBICACION="IdUbicacion";

    //RESPUESTAS
    public static final String API_URL_PATH_RESPUESTAS = "/Encuestas/Respuestas/Create";
    public static final String API_PARAM_NAME_RESPUESTAS_IDALTERNATIVA = "IdAlternativa";


    //UBICACION
    public static final String API_URL_PATH_UBICACIONES = "/Ubicaciones/List";
    public static final String API_URL_PATH_UBICACIONES_FIND = "/Ubicaciones/Find/{IdUbicacion}";

    // DESTINATARIOS
    public static final String API_URL_PATH_DESTINATARIO_FIND = "/Destinatarios/Find/{movil}";


    // HEADERS
    public static final String API_HEADER_CONTENT_TYPE_NAME = "Content-Type";
    public static final String API_HEADER_CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded";
    public static final String API_HEADER_CONTENT_TYPE_VALUE2= "application/json";
    public static final String API_HEADER_AUTHORIZATION_NAME = "Authorization";


    //ENVIO SMS
    public static final String API_URL_PATH_POST_SMS="/SMS/SingleSMS";
    public static final String API_PARAM_NAME_SINGLESMS_DESTINO="Destino";
    public static final String API_PARAM_NAME_SINGLESMS_MENSAJE="Mensaje";
    public static final String API_PARAM_NAME_SINGLESMS_PROGRAMADO="Programado";
    public static final String API_PARAM_NAME_SINGLESMS_FH_PROGRAMADO="FH_Programacion";

    //extra
    public static final String API_TOKEN_TYPE= "Bearer";
    public static final String LINK_ENCUESTA_RESPONSE="https://portal.ketito.cl/SurveyQRTake?id=";

}
