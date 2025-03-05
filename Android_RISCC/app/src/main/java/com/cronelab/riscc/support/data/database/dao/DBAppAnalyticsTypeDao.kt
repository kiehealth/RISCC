package com.cronelab.riscc.support.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.cronelab.riscc.support.data.database.table.DBAppAnalyticsType
import com.cronelab.riscc.support.data.database.configuration.DatabaseConfigs

@Dao
interface DBAppAnalyticsTypeDao : BaseDao<DBAppAnalyticsType> {

    @Query("SELECT * FROM " + DatabaseConfigs.tbl_app_analytics_type)
    fun getAllAppAnalyticsType(): List<DBAppAnalyticsType>

//    @Query("SELECT * FROM " + DatabaseConfigs.tbl_links + " WHERE title LIKE :searchTerms")
//    fun getMatchingLinks(searchTerms: String): List<DBLinks>


    @Query("SELECT * FROM " + DatabaseConfigs.tbl_app_analytics_type + " WHERE title = :key")
    fun getAppAnalyticsType(key: String): List<DBAppAnalyticsType>

}