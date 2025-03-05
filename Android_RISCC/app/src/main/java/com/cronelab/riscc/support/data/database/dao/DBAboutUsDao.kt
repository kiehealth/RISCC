package com.cronelab.riscc.support.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.cronelab.riscc.support.data.database.configuration.DatabaseConfigs
import com.cronelab.riscc.support.data.database.table.DBAboutUs

@Dao
interface DBAboutUsDao : BaseDao<DBAboutUs> {

    @Query("SELECT * FROM " + DatabaseConfigs.tbl_about_us)
    fun getAboutUs(): List<DBAboutUs>

}