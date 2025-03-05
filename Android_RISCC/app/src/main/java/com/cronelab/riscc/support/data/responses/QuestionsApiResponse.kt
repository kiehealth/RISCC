package com.cronelab.riscc.support.data.responses

import com.cronelab.riscc.support.data.database.table.questions.DBQuestions
import com.cronelab.riscc.ui.questionnaire.pojo.WelcomeQuestionnaire
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class QuestionsApiResponse {
    @SerializedName("status")
    @Expose
    var status = false

    @SerializedName("message")
    @Expose
    var message=""


    @SerializedName("data")
    @Expose
    var data: Data? = null



    class Data {
        @SerializedName("list")
        @Expose
        var questionsList: List<DBQuestions>? = null
        @SerializedName("welcome")
        @Expose
        var welcome: WelcomeQuestionnaire? = null
    }
}