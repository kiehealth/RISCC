package com.cronelab.riscc.support.data.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cronelab.riscc.support.data.database.configuration.DatabaseConfigs
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity(tableName = DatabaseConfigs.tbl_user)
class DBUser : Serializable {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NotNull
    var id: String = ""

    @SerializedName("firstName")
    @Expose
    var firstName: String=""

    @SerializedName("lastName")
    @Expose
    var lastName: String=""

    @SerializedName("mobileNumber")
    @Expose
    var mobileNumber: String=""

    @SerializedName("emailAddress")
    @Expose
    var emailAddress: String=""

    @SerializedName("imageUrl")
    @Expose
    var imageUrl: String=""

    @SerializedName("hasAnsweredQuestionnaire")
    @Expose
    var hasAnsweredQuestionnaire: Boolean? = null

    var token = ""
    var imageEncoded: String=""
    var password: String=""

}


