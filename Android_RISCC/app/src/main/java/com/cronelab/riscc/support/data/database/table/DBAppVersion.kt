package com.cronelab.riscc.support.data.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cronelab.riscc.support.data.database.configuration.DatabaseConfigs
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull
import java.io.Serializable


@Entity(tableName = DatabaseConfigs.tbl_app_version)
class DBAppVersion : Serializable {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NotNull
    var id=0 

    @SerializedName("family")
    @Expose
    var family: String? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    @SerializedName("forceUpdate")
    @Expose
    var forceUpdate: Boolean? = null
}