package com.cronelab.riscc.support.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.cronelab.riscc.support.data.database.configuration.DatabaseConfigs
import com.cronelab.riscc.support.data.database.table.DBAppVersion

@Dao
interface DBAppVersionDao : BaseDao<DBAppVersion> {

    @Query("SELECT * FROM " + DatabaseConfigs.tbl_app_version)
    fun getAppVersion(): List<DBAppVersion>

}