package com.cronelab.riscc.support.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.cronelab.riscc.support.data.database.configuration.DatabaseConfigs
import com.cronelab.riscc.support.data.database.table.DBNotification

@Dao
interface DBNotificationDao : BaseDao<DBNotification> {

    @Query("SELECT * FROM " + DatabaseConfigs.tbl_notification)
    fun getAllNotification(): List<DBNotification>
}