package com.cronelab.riscc.support.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.cronelab.riscc.support.data.database.configuration.DatabaseConfigs
import com.cronelab.riscc.support.data.database.table.DBNote

@Dao
interface DBNoteDao : BaseDao<DBNote> {

    @Query("SELECT * FROM " + DatabaseConfigs.tbl_note)
    fun getAllNote(): List<DBNote>

//    @Query("SELECT * FROM " + DatabaseConfigs.tbl_note + " WHERE id = :id")
//    fun deleteNote(id: String)

    @Delete
    fun deleteNote(model: DBNote)
}