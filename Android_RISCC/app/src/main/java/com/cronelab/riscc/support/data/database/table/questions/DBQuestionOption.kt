package com.cronelab.riscc.support.data.database.table.questions

import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull
import java.io.Serializable

//@Entity(tableName = DatabaseConfigs.tbl_questionOption)
class DBQuestionOption : Serializable {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NotNull
    var questionOptionId = 0

    @SerializedName("title")
    @Expose
    var optionTitle: String? = null

    @SerializedName("value")
    @Expose
    var optionValue: String? = null
}