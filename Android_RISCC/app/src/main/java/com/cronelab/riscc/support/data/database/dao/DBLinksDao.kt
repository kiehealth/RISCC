package com.cronelab.riscc.support.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.cronelab.riscc.support.data.database.configuration.DatabaseConfigs
import com.cronelab.riscc.support.data.database.table.DBLinks

@Dao
interface DBLinksDao : BaseDao<DBLinks> {

    @Query("SELECT * FROM " + DatabaseConfigs.tbl_links)
    fun getAllLinks(): List<DBLinks>

    @Query("SELECT * FROM " + DatabaseConfigs.tbl_links + " WHERE title LIKE :searchTerms")
    fun getMatchingLinks(searchTerms: String): List<DBLinks>
}