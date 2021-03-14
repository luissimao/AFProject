package ipvc.estg.afproject.db

import androidx.lifecycle.LiveData
import ipvc.estg.afproject.dao.NotaDao
import ipvc.estg.afproject.entities.Nota

class NotaRepository(private val datarecordDao: NotaDao) {

    val allItems: LiveData<List<Nota>> = datarecordDao.getall()

    fun get(id: Long):LiveData<Nota> {
        return datarecordDao.get(id)
    }

    suspend fun update(item: Nota) {
        datarecordDao.update(item)
    }

    suspend fun insert(item: Nota) {
        datarecordDao.insert(item)
    }

    suspend fun delete(item: Nota) {
        datarecordDao.delete(item)
    }
}