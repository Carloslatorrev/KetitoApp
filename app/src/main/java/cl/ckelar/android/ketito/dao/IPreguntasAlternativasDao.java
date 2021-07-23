package cl.ckelar.android.ketito.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.ckelar.android.ketito.dto.PreguntasAlternativas;

/**
 * Interface para Querys a la base de datos
 */
@Dao
public interface IPreguntasAlternativasDao {

    @Insert
    void insertPreguntasAlternativas(PreguntasAlternativas preguntasAlternativas);

    @Query("SELECT * FROM PreguntasAlternativas")
    List<PreguntasAlternativas> getAll();

    @Query("SELECT * FROM PreguntasAlternativas WHERE IdAlternativa= :IdAlternativa")
    PreguntasAlternativas getPreguntaAlternativaById(int IdAlternativa);

    @Query("SELECT * FROM PreguntasAlternativas WHERE IdPregunta= :IdPregunta")
    PreguntasAlternativas getPreguntaAlternativaByIdPregunta(int IdPregunta);

    @Query("SELECT * FROM PreguntasAlternativas WHERE IdPregunta= :IdPregunta")
    List<PreguntasAlternativas> getListPreguntasAlternativasByIdPregunta(int IdPregunta);

    @Update
    void updatePreguntasAlternativas(PreguntasAlternativas preguntasAlternativas);

    @Delete
    void deletePreguntasAlternativas(PreguntasAlternativas preguntasAlternativas);


}
