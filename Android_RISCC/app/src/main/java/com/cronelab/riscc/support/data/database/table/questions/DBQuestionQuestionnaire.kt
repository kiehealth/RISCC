package com.cronelab.riscc.support.data.database.table.questions

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DBQuestionQuestionnaire : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("displayOrder")
    @Expose
    var displayOrder: Int? = null

    @SerializedName("questionnaire")
    @Expose
    var questionnaire: Questionnaire? = null

    @SerializedName("question")
    @Expose
    var question: Question? = null
}