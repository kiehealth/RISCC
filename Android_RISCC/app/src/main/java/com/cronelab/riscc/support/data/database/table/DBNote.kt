package com.cronelab.riscc.support.data.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cronelab.riscc.support.data.database.configuration.DatabaseConfigs
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity(tableName = DatabaseConfigs.tbl_note)
class DBNote : Serializable {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NotNull
    var id = ""

    @SerializedName("title")
    @Expose
    var title: String=""

    @SerializedName("description")
    @Expose
    var description: String=""

    @SerializedName("createdDateTime")
    @Expose
    var createdDateTime: String? = null

    var isSynced = true
}