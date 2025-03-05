package com.cronelab.riscc.support.data.database.table.questions

import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull
import java.io.Serializable

//@Entity(tableName = DatabaseConfigs.tbl_Questionnaire)
class DBQuestionnaire : Serializable {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NotNull
    var questionnaireId = 0

    @SerializedName("title")
    @Expose
    var title: String=""
}