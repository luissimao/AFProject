package ipvc.estg.afproject.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ipvc.estg.afproject.entities.Nota

@Dao
interface NotaDao {

    @Query("SELECT * from notas")
    fun getall(): LiveData<List<Nota>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: Nota)

    @Query("SELECT * FROM notas WHERE notas.id == :id")
    fun get(id: Long): LiveData<Nota>

    @Update
    suspend fun update(vararg items: Nota)

    @Delete
    suspend fun delete(vararg items: Nota)
}
