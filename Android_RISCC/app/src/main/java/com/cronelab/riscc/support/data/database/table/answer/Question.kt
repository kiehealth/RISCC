package com.cronelab.riscc.support.data.database.table.answer

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Question {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("body")
    @Expose
    var body: String? = null

    @SerializedName("questionType")
    @Expose
    var questionType: QuestionType? = null
}