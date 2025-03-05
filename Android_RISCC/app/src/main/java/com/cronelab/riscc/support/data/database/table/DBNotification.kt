package com.cronelab.riscc.support.data.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cronelab.riscc.support.data.database.configuration.DatabaseConfigs
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull
import java.io.Serializable


@Entity(tableName = DatabaseConfigs.tbl_notification)
class DBNotification : Serializable {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NotNull
    var id = ""

    @SerializedName("title")
    @Expose
    var title = ""

    @SerializedName("description")
    @Expose
    var description= ""

    @SerializedName("notificationType")
    @Expose
    var notificationType= ""

    @SerializedName("dateTime")
    @Expose
    var dateTime= ""

   /* @SerializedName("users")
    @Expose
    var users: List<User>? = null*/
}