package ipvc.estg.afproject.db

import androidx.lifecycle.LiveData
import ipvc.estg.afproject.dao.NoteDao
import ipvc.estg.afproject.entities.Note

class NoteRepository(private val noteDao: NoteDao) {

    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

}