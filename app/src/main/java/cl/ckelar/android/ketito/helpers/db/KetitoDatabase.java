package cl.ckelar.android.ketito.helpers.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import cl.ckelar.android.ketito.dao.IEmpresaDao;
import cl.ckelar.android.ketito.dao.IEncuestaDao;
import cl.ckelar.android.ketito.dao.ILinkEncuestaDao;
import cl.ckelar.android.ketito.dao.IPreguntasAlternativasDao;
import cl.ckelar.android.ketito.dao.IPreguntasDao;
import cl.ckelar.android.ketito.dao.IRespuestasDao;
import cl.ckelar.android.ketito.dao.ISettingUserDao;
import cl.ckelar.android.ketito.dao.ISingleSMSDao;
import cl.ckelar.android.ketito.dao.ITokenDao;
import cl.ckelar.android.ketito.dao.ITomaDao;
import cl.ckelar.android.ketito.dao.IUbicacionDao;
import cl.ckelar.android.ketito.dao.IUserSesionDao;
import cl.ckelar.android.ketito.dto.Empresa;
import cl.ckelar.android.ketito.dto.Encuesta;
import cl.ckelar.android.ketito.dto.LinkEncuesta;
import cl.ckelar.android.ketito.dto.Preguntas;
import cl.ckelar.android.ketito.dto.PreguntasAlternativas;
import cl.ckelar.android.ketito.dto.Respuesta;
import cl.ckelar.android.ketito.dto.SettingUser;
import cl.ckelar.android.ketito.dto.SingleSMS;
import cl.ckelar.android.ketito.dto.TokenC;
import cl.ckelar.android.ketito.dto.Toma;
import cl.ckelar.android.ketito.dto.Ubicacion;
import cl.ckelar.android.ketito.dto.UserSesion;

@Database(entities = {UserSesion.class, Empresa.class, Encuesta.class, TokenC.class, PreguntasAlternativas.class, Preguntas.class, LinkEncuesta.class,
        SettingUser.class, Toma.class, Respuesta.class, Ubicacion.class, SingleSMS.class}, version = 1, exportSchema = false)
public abstract class KetitoDatabase extends RoomDatabase {

    public abstract IUserSesionDao userSesionDao();
    public abstract IEmpresaDao empresaDao();
    public abstract IEncuestaDao encuestaDao();
    public abstract ITokenDao tokenDao();
    public abstract IPreguntasAlternativasDao preguntasAlternativasDao();
    public abstract IPreguntasDao preguntasDao();
    public abstract ILinkEncuestaDao linkEncuestaDao();
    public abstract ISettingUserDao settingUserDao();
    public abstract ITomaDao tomaDao();
    public abstract IUbicacionDao ubicacionDao();
    public abstract IRespuestasDao respuestasDao();
    public abstract ISingleSMSDao singleSMSDao();

    private static KetitoDatabase ketitoDB;
    private static final String DATABASE_NAME = "ketito_db";

    /**
     * @param context contexto del activity actual
     * @return retorna instancia de la base de datos local
     */
    public static KetitoDatabase getInstance(Context context) {
        if (null == ketitoDB) {
            ketitoDB = buildDatabaseInstance(context);
        }
        return ketitoDB;
    }

    /**
     * @param context contexto del activity actual
     * @return retorna instancia de la base de datos local
     */
    private static KetitoDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                KetitoDatabase.class,
                DATABASE_NAME)
                .allowMainThreadQueries().build();
    }

    public void cleanUp(){
        ketitoDB = null;
    }

}
