package cl.ckelar.android.ketito.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import cl.ckelar.android.ketito.dto.UserSesion;

/**
 * Interface para Querys a la base de datos
 */
@Dao
public interface IUserSesionDao {

    @Insert
    void insertUserSesion(UserSesion userSesion);

    @Query("SELECT * FROM UserSesion WHERE access_token= :access_token")
    UserSesion getUserSesion(String access_token);

    @Query("SELECT * FROM UserSesion LIMIT 1")
    UserSesion getUserSesionFirst();

    @Update
    void updateUserSesion(UserSesion userSesion);

    @Delete
    void deleteUserSesion(UserSesion userSesion);

}
