package cl.ckelar.android.ketito.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.ckelar.android.ketito.dto.Toma;

/**
 * Interface para Querys a la base de datos
 */
@Dao
public interface ITomaDao {

    @Insert
    void insertToma(Toma toma);

    @Query("SELECT * FROM Toma")
    List<Toma> getAll();

    @Query("SELECT * FROM Toma WHERE IdToma= :IdToma")
    Toma getTomaById(int IdToma);

    @Query("SELECT * FROM Toma WHERE IdEncuesta= :IdEncuesta")
    Toma getTomaByEncuesta(String IdEncuesta);

    @Query("SELECT * FROM Toma WHERE IdEncuesta= :IdEncuesta")
    List<Toma> getTomaListByEncuesta(String IdEncuesta);

    @Query("SELECT * FROM Toma WHERE FH_Toma= :FH_Toma")
    Toma getTomaByFechaInicio(String FH_Toma);

    @Update
    void updateToma(Toma toma);

    @Delete
    void deleteToma(Toma toma);
}
