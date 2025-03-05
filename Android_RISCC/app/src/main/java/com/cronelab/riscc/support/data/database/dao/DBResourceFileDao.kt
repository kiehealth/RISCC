package com.cronelab.riscc.support.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.cronelab.riscc.support.data.database.configuration.DatabaseConfigs
import com.cronelab.riscc.support.data.database.table.DBResourceFile

@Dao
interface DBResourceFileDao : BaseDao<DBResourceFile> {

    @Query("SELECT * FROM " + DatabaseConfigs.tbl_resource_file)
    fun getResourceFiles(): List<DBResourceFile>

    @Query("SELECT url FROM " + DatabaseConfigs.tbl_resource_file + " WHERE title = :title")
    fun getResourceUrlFromTitle(title:String): String

    @Query("SELECT * FROM " + DatabaseConfigs.tbl_resource_file + " WHERE title = :title")
    fun getResourceFile(title:String): DBResourceFile

}