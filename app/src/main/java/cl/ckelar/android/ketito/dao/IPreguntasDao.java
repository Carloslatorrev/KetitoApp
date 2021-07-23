package cl.ckelar.android.ketito.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.ckelar.android.ketito.dto.Preguntas;

/**
 * Interface para Querys a la base de datos
 */
@Dao
public interface IPreguntasDao {

    @Insert
    void insertPreguntas(Preguntas preguntas);

    @Query("SELECT * FROM Preguntas")
    List<Preguntas> getAll();

    @Query("SELECT * FROM Preguntas WHERE IdPregunta= :IdPregunta")
    Preguntas getPreguntasById(int IdPregunta);

    @Query("SELECT * FROM Preguntas WHERE IdEncuesta= :IdEncuesta")
    Preguntas getPreguntasByEncuesta(String IdEncuesta);

    @Query("SELECT * FROM Preguntas WHERE IdEncuesta= :IdEncuesta")
    List<Preguntas> getPreguntasAllByEncuesta(String IdEncuesta);

    @Update
    void updatePreguntas(Preguntas preguntas);

    @Delete
    void deletePreguntas(Preguntas preguntas);

}
