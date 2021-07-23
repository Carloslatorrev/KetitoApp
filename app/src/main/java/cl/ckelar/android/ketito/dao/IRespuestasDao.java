package cl.ckelar.android.ketito.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.ckelar.android.ketito.dto.Respuesta;

/**
 * Interface para Querys a la base de datos
 */
@Dao
public interface IRespuestasDao {

    @Insert
    void insertRespuesta(Respuesta respuesta);

    @Query("SELECT * FROM Respuesta")
    List<Respuesta> getAll();

    @Query("SELECT * FROM Respuesta WHERE IdRespuesta= :IdRespuesta")
    Respuesta getRespuestaById(String IdRespuesta);

    @Query("SELECT * FROM Respuesta WHERE IdToma= :IdToma")
    Respuesta getRespuestaByEncuesta(String IdToma);

    @Query("SELECT * FROM Respuesta WHERE IdPregunta= :IdPregunta")
    List<Respuesta> getRespuestaByIdPregunta(int IdPregunta);

    @Update
    void updateRespuesta(Respuesta respuesta);

    @Delete
    void deleteRespuesta(Respuesta respuesta);
}
