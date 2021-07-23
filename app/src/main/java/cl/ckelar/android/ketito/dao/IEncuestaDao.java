package cl.ckelar.android.ketito.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.ckelar.android.ketito.dto.Encuesta;

/**
 * Interface para Querys a la base de datos
 */
@Dao
public interface IEncuestaDao {

    @Insert
    void insertEncuesta(Encuesta encuesta);

    @Query("SELECT * FROM Encuesta")
    List<Encuesta> getAll();

    @Query("SELECT * FROM Encuesta WHERE idEncuesta= :idEncuesta")
    Encuesta getEncuestaById(String idEncuesta);

    @Query("SELECT * FROM Encuesta WHERE titulo= :titulo")
    Encuesta getEncuestaByTitulo(String titulo);

    @Query("SELECT * FROM Encuesta WHERE Selected= :Selected")
    Encuesta getEncuestaBySelected(Boolean Selected);

    @Update
    void updateEncuesta(Encuesta encuesta);

    @Delete
    void deleteEncuesta(Encuesta encuesta);

}
