package cl.ckelar.android.ketito.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.ckelar.android.ketito.dto.Ubicacion;

/**
 * Interface para Querys a la base de datos
 */
@Dao
public interface IUbicacionDao {

    @Insert
    void insertUbicacion(Ubicacion ubicacion);

    @Query("SELECT * FROM Ubicacion")
    List<Ubicacion> getAll();

    @Query("SELECT * FROM Ubicacion WHERE idUbicacion= :idUbicacion")
    Ubicacion getUbicacionById(String idUbicacion);

    @Query("SELECT * FROM Ubicacion WHERE Ubicacion= :ubicacion")
    Ubicacion getUbicacionByNombre(String ubicacion);

    @Query("SELECT * FROM Ubicacion WHERE Selected= :Selected")
    Ubicacion getUbicacionBySelected(Boolean Selected);

    @Update
    void updateUbicacion(Ubicacion ubicacion);

    @Delete
    void deleteUbicacion(Ubicacion ubicacion);

}
