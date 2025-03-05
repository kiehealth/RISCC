package com.cronelab.riscc.support.data.database.table.questions

import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull
import java.io.Serializable

//@Entity(tableName = DatabaseConfigs.tbl_Questions)
class DBQuestions : Serializable {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NotNull
    var id = 0

    var groupQuestionnaireId = 0

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("body")
    @Expose
    var body: String? = null

    @SerializedName("questionType")
    @Expose
    var dbQuestionType: DBQuestionType? = null

    @SerializedName("questionOptions")
    @Expose
    var dbQuestionOptions: List<DBQuestionOption>? = null

    @SerializedName("questionQuestionnaires")
    @Expose
    var dbQuestionQuestionnaires: List<DBQuestionQuestionnaire>? = null

}