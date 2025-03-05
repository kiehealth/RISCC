package com.cronelab.riscc.support.data.database.table.answer

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class QuestionType {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("title")
    @Expose
    var title: String? = null
}