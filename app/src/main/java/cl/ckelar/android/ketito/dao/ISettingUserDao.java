package cl.ckelar.android.ketito.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.ckelar.android.ketito.dto.SettingUser;

/**
 * Interface para Querys a la base de datos
 */
@Dao
public interface ISettingUserDao {

    @Insert
    void insertSettingUser(SettingUser settingUser);

    @Query("SELECT * FROM SettingUser")
    List<SettingUser> getAll();

    @Query("SELECT * FROM SettingUser WHERE IdSetting= :IdSetting")
    SettingUser getSettingUserById(int IdSetting);

    @Query("SELECT * FROM SettingUser WHERE EnvioSMS= :EnvioSMS")
    SettingUser getSettingUserByEnvioSMS(Boolean EnvioSMS);

    @Query("SELECT * FROM SettingUser WHERE Password= :Password")
    SettingUser getSettingUserByPassword(String Password);

    @Update
    void updateSettingUser(SettingUser settingUser);

    @Delete
    void deleteSettingUSer(SettingUser settingUser);

}
