package cl.ckelar.android.ketito.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.ckelar.android.ketito.dto.Empresa;

/**
 * Interface para Querys a la base de datos
 */
@Dao
public interface IEmpresaDao {

    @Insert
    void insertEmpresa(Empresa empresa);

    @Query("SELECT * FROM Empresa")
    List<Empresa> getAll();

    @Query("SELECT * FROM Empresa WHERE IdEmpresa= :IdEmpresa")
    Empresa getEmpresaById(String IdEmpresa);

    @Query("SELECT * FROM Empresa WHERE Selected=1")
    Empresa getEmpresaBySelected();

    @Query("SELECT * FROM Empresa WHERE Razon= :Razon")
    Empresa getEmpresaByRazon(String Razon);

    @Update
    void updateEmpresa(Empresa empresa);

}
