package cl.ckelar.android.ketito.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.ckelar.android.ketito.dto.TokenC;

/**
 * Interface para Querys a la base de datos
 */
@Dao
public interface ITokenDao {

    @Insert
    void InsertToken(TokenC tokenC);

    @Query("SELECT * FROM TokenC")
    List<TokenC> getAll();


    @Query("SELECT * FROM TokenC WHERE Selected=1")
    TokenC getTokenBySelected();

    @Query("SELECT * FROM TokenC WHERE TokenName = :Nombre")
    TokenC getTokenByNombre(String Nombre);

    @Query("SELECT * FROM TokenC WHERE Token = :Token")
    TokenC getTokenByToken(String Token);

    @Update
    void updateTokenC(TokenC tokenC);

    @Delete
    void deleteTokenC(TokenC tokenC);

}
