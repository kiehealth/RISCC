package com.cronelab.riscc.support.data.database.table.answer

import com.cronelab.riscc.support.data.database.table.DBUser
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Answer {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("answer")
    @Expose
    var answer: String? = null

    @SerializedName("answerOptions")
    @Expose
    var answerOptions: List<AnswerOption>? = null

    @SerializedName("question")
    @Expose
    var question: Question? = null

    @SerializedName("user")
    @Expose
    var user: DBUser? = null

    @SerializedName("dateTime")
    @Expose
    var dateTime: String? = null
}