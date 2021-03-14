package ipvc.estg.afproject.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ipvc.estg.afproject.db.AppRoomDatabase
import ipvc.estg.afproject.db.NotaRepository
import ipvc.estg.afproject.entities.Nota
import kotlinx.coroutines.launch

private const val TAG = "DataRecordViewModel "

class NotasViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotaRepository
    val allItems: LiveData<List<Nota>>

    init {
        Log.d(TAG, "Inside ViewModel init")
        val dao = AppRoomDatabase.getDatabase(application).notaDao()
        repository = NotaRepository(dao)
        allItems = repository.allItems
    }

    fun insert(item: Nota) = viewModelScope.launch {
        repository.insert(item)
    }

    fun update(item: Nota) = viewModelScope.launch {
        repository.update(item)
    }

    fun delete(item: Nota) = viewModelScope.launch {
        repository.delete(item)
    }

    fun get(id: Long) = repository.get(id)
}