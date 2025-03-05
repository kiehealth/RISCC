package com.cronelab.riscc.ui.note.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cronelab.riscc.support.data.database.table.DBNote
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.ui.note.model.NoteRepository.*
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    var noteList = MutableLiveData<List<DBNote>>()
    var noteResponse = MutableLiveData<NoteResponse>()
    var notePostResponse = MutableLiveData<NotePostResponse>()
    var noteDeleteResponse = MutableLiveData<NoteDeleteResponse>()

    var deleteNoteStatus = MutableLiveData<Boolean>()
    var updatedNoteList = MutableLiveData<List<DBNote>>()
    var noteUpdateResponse = MutableLiveData<NoteUpdateResponse>()

    fun getNotes(dbUser: DBUser, page: Int, size: Int) {
        viewModelScope.launch {
            try {
                noteResponse.postValue(repository.getNotes(dbUser, page, size))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun getNotesFromDB() {
        noteList = MutableLiveData<List<DBNote>>()
        viewModelScope.launch {
            try {
                noteList.postValue(repository.getNotesFromDB())
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    /* fun deleteNotesFromDB(dbNote: DBNote) {
         viewModelScope.launch {
             try {
                 deleteNoteStatus.postValue(repository.deleteNoteFromDB(dbNote))
             } catch (ex: Exception) {
                 ex.printStackTrace()
             }
         }
     }*/

    fun sendNote(dbUser: DBUser, title: String, description: String) {
        viewModelScope.launch {
            try {
                notePostResponse.postValue(repository.sendNote(dbUser, title, description))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun deleteNote(dbUser: DBUser, dbNote: DBNote) {
        viewModelScope.launch {
            try {
                noteDeleteResponse.postValue(repository.deleteNote(dbUser, dbNote))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun updateNoteInDB(dbNote: DBNote) {
        viewModelScope.launch {
            try {
                updatedNoteList.postValue(repository.updateNoteInDB(dbNote))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun updateNoteInServer(dbUser: DBUser, dbNote: DBNote) {
        viewModelScope.launch {
            try {
                noteUpdateResponse.postValue(repository.updateNoteInServer(dbUser, dbNote))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }


}