package com.cronelab.riscc.support.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.cronelab.riscc.support.data.database.configuration.DatabaseConfigs
import com.cronelab.riscc.support.data.database.table.DBUser

@Dao
interface DBUserDao : BaseDao<DBUser> {

//        @Query("SELECT * FROM " + DatabaseConfigs.tbl_user)
//    fun getAllUsers(): LiveData<DBUser>
//
//    @Query("SELECT * FROM " + DatabaseConfigs.tbl_user)
//    fun getAllUsers(): MutableLiveData<DBUser>

    @Query("SELECT * FROM " + DatabaseConfigs.tbl_user)
    fun getAllUsers(): List<DBUser>

    @Query("DELETE FROM " + DatabaseConfigs.tbl_user)
    fun deleteAll()
}