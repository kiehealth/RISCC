package com.cronelab.riscc.support.data.database.table.questions

import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull
import java.io.Serializable

//@Entity(tableName = DatabaseConfigs.tbl_ActiveGroupQuestionnaire)
class DBActiveQuestionnaireGroup : Serializable {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NotNull
    var id = 0

    @SerializedName("startDateTime")
    @Expose
    var startDateTime: String? = null

    @SerializedName("endDateTime")
    @Expose
    var endDateTime: String? = null

    @SerializedName("reminderMessage")
    @Expose
    var reminderMessage: String? = null

    @SerializedName("reminderTimeInterval")
    @Expose
    var reminderTimeInterval: Int? = null

    @SerializedName("questionnaire")
    @Expose
    var questionnaire: DBQuestionnaire? = null

}