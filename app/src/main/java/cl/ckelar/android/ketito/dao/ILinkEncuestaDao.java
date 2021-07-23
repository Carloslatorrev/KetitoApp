package cl.ckelar.android.ketito.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.ckelar.android.ketito.dto.LinkEncuesta;

/**
 * Interface para Querys a la base de datos
 */
@Dao
public interface ILinkEncuestaDao {

    @Insert
    void insertLinkEncuesta(LinkEncuesta linkEncuesta);

    @Query("SELECT * FROM LinkEncuesta")
    List<LinkEncuesta> getAll();

    @Query("SELECT * FROM LinkEncuesta WHERE IdLinkEncuesta= :IdLinkEncuesta")
    LinkEncuesta getLinkEncuestaById(String IdLinkEncuesta);

    @Query("SELECT * FROM LinkEncuesta WHERE IdEncuesta= :IdEncuesta")
    LinkEncuesta getLinkEncuestaByEncuesta(String IdEncuesta);

    @Update
    void updateLinkEncuesta(LinkEncuesta linkEncuesta);

    @Delete
    void deleteLinkEncuesta(LinkEncuesta linkEncuesta);
}
