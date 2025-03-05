package com.cronelab.riscc.support.data.database.table.answer

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class AnswerOption {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("value")
    @Expose
    var value: String? = null
}