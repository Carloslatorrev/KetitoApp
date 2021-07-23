package cl.ckelar.android.ketito.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.ckelar.android.ketito.dto.SingleSMS;

/**
 * Interface para Querys a la base de datos
 */
@Dao
public interface ISingleSMSDao {
    @Insert
    void insertSingleSMS(SingleSMS singleSMS);

    @Query("SELECT * FROM SingleSMS")
    List<SingleSMS> getAll();

    @Query("SELECT * FROM SingleSMS WHERE IdSingleSMS= :IdSingleSMS")
    SingleSMS getSingleSMSById(String IdSingleSMS);

    @Query("SELECT * FROM SingleSMS WHERE Destino= :Destino")
    SingleSMS getSingleSMSByDestinoSMS(String Destino);


    @Update
    void updateSingleSMS(SingleSMS singleSMS);

    @Delete
    void deleteSingleSMS(SingleSMS singleSMS);
}
