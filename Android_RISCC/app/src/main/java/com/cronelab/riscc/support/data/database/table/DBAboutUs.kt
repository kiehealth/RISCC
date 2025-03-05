package com.cronelab.riscc.support.data.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cronelab.riscc.support.data.database.configuration.DatabaseConfigs
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity(tableName = DatabaseConfigs.tbl_about_us)
class DBAboutUs {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NotNull
    var id=0

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null
}