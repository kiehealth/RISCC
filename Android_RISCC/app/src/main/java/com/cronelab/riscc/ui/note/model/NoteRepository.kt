package com.cronelab.riscc.ui.note.model

import android.util.Log
import com.cronelab.riscc.R
import com.cronelab.riscc.support.AppController
import com.cronelab.riscc.support.data.ApiFactory
import com.cronelab.riscc.support.data.SafeApiRequest
import com.cronelab.riscc.support.data.database.configuration.AppDatabase
import com.cronelab.riscc.support.data.database.table.DBNote
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.helper.Coroutines
import com.cronelab.riscc.support.manager.LanguageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(
    private val apiFactory: ApiFactory,
    private val appDatabase: AppDatabase
) : SafeApiRequest() {

    class NoteResponse {
        var noteList: List<DBNote> = ArrayList()
        var error: Throwable? = null
        var message = ""
        var totalPage = 0
        var endPosition = 0
        var totalRecord = 0
    }

    class NotePostResponse {
        var note = DBNote()
        var error: Throwable? = null
        var message = ""
    }

    class NoteDeleteResponse {
        var error: Throwable? = null
        var message = ""
    }

    class NoteUpdateResponse {
        var error: Throwable? = null
        var message = ""
    }

    private var noteResponse = NoteResponse()
    private var notePostResponse = NotePostResponse()
    private var noteDeleteResponse = NoteDeleteResponse()
    private var noteUpdateResponse = NoteUpdateResponse()

    suspend fun getNotes(dbUser: DBUser, page: Int, size: Int): NoteResponse {
        return withContext(Dispatchers.IO) {
            getNotesFromServer(dbUser, page, size)
            noteResponse
        }
    }

    private suspend fun getNotesFromServer(dbUser: DBUser, page: Int, size: Int) {
        val noteResponse = NoteResponse()
        try {
            val apiResponse = apiRequest { apiFactory.getNote(dbUser.token, LanguageManager.getLanguageCode(), page, size) }
            if (apiResponse.status) {
                noteResponse.noteList = apiResponse.data!!.list!!
                noteResponse.totalPage = apiResponse.totalPage
                noteResponse.endPosition = apiResponse.endPosition
                noteResponse.totalRecord = apiResponse.totalRecord
                saveNotes(noteResponse.noteList)
                noteResponse.error = null
            } else {
                noteResponse.error = Throwable()
            }
            noteResponse.message = apiResponse.message
        } catch (ex: Exception) {
            ex.printStackTrace()
            noteResponse.error = ex
            noteResponse.message = "Unable to get notes"
        }
        this.noteResponse = noteResponse
    }

    suspend fun sendNote(dbUser: DBUser, title: String, description: String): NotePostResponse {
        return withContext(Dispatchers.IO) {
            postNote(dbUser, title, description)
            notePostResponse
        }
    }

    private suspend fun postNote(dbUser: DBUser, title: String, description: String) {
        val notePostResponse = NotePostResponse()
        val body = HashMap<String, String>()
        body["description"] = description
        body["title"] = title
        body["userId"] = dbUser.id
        try {
            val apiResponse = apiRequest { apiFactory.postNote(dbUser.token, LanguageManager.getLanguageCode(), body) }
            notePostResponse.message = apiResponse.message
            if (apiResponse.status) {
                notePostResponse.note = apiResponse.data!!.note!!
                saveNote(notePostResponse.note)
                notePostResponse.error = null
            } else {
                notePostResponse.error = Throwable()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            notePostResponse.error = ex
            notePostResponse.message = "Unable to save the note"
        }
        this.notePostResponse = notePostResponse
    }

    private fun saveNote(note: DBNote) {
        Coroutines.io {
            appDatabase.getDBNoteDao().insert(note)
        }
    }

    private fun saveNotes(noteList: List<DBNote>) {
        Coroutines.io {
            appDatabase.getDBNoteDao().insert(noteList)
        }
    }

    suspend fun getNotesFromDB(): List<DBNote>? {
        return withContext(Dispatchers.IO) {
            appDatabase.getDBNoteDao().getAllNote()
        }
    }

    suspend fun deleteNote(dbUser: DBUser, dbNote: DBNote): NoteDeleteResponse {
        return withContext(Dispatchers.IO) {
            deleteNoteFromDB(dbNote)
            deleteNoteFromServer(dbUser, dbNote)
            noteDeleteResponse
        }
    }

    suspend fun updateNoteInDB(dbNote: DBNote): List<DBNote> {
        return withContext(Dispatchers.IO) {
            appDatabase.getDBNoteDao().update(dbNote)
            appDatabase.getDBNoteDao().getAllNote()
        }
    }

    suspend fun updateNoteInServer(dbUser: DBUser, dbNote: DBNote): NoteUpdateResponse {
        return withContext(Dispatchers.IO) {
            requestUpdateNote(dbUser, dbNote)
            noteUpdateResponse
        }
    }

    private suspend fun requestUpdateNote(dbUser: DBUser, dbNote: DBNote) {
        val noteUpdateResponse = NoteUpdateResponse()
        try {
            val params = HashMap<String, String>()
            params["userId"] = dbUser.id
            params["id"] = dbNote.id
            params["title"] = dbNote.title
            params["description"] = dbNote.description
            val apiResponse = apiRequest { apiFactory.updateNote(dbUser.token, LanguageManager.getLanguageCode(), params) }
            noteUpdateResponse.message = apiResponse.message
            if (apiResponse.status) {
                noteUpdateResponse.error = null
            } else {
                noteUpdateResponse.error = Throwable()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            noteUpdateResponse.error = ex
            Log.e("NR", "ex: " + ex.message)
            val errorMessage = ex.message + ""
            if (errorMessage.contains("Failed to connect")) {
                noteUpdateResponse.message = AppController.instance.applicationContext.getString(R.string.noteWillBeSyncOnInternetRestore)
            } else {
                noteUpdateResponse.message = AppController.instance.applicationContext.getString(R.string.unableToSyncNote)
            }
        }
        this.noteUpdateResponse = noteUpdateResponse
    }

    private suspend fun deleteNoteFromServer(dbUser: DBUser, dbNote: DBNote) {
        val noteDeleteResponse = NoteDeleteResponse()
        try {
            Log.d("NoteRepository", "NoteId: ${dbNote.id}")
            val apiResponse = apiRequest { apiFactory.deleteNote(dbUser.token, LanguageManager.getLanguageCode(), dbNote.id) }
            noteDeleteResponse.message = apiResponse.message
            if (apiResponse.status) {
                noteDeleteResponse.error = null
            } else {
                noteDeleteResponse.error = Throwable()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            noteDeleteResponse.error = ex
            noteDeleteResponse.message = "Unable to delete note"
        }
        this.noteDeleteResponse = noteDeleteResponse
    }

    private suspend fun deleteNoteFromDB(dbNote: DBNote) {
        return withContext(Dispatchers.IO) {
            appDatabase.getDBNoteDao().deleteNote(dbNote)
        }
    }
}