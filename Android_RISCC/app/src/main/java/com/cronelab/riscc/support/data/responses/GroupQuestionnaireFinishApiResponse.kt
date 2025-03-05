package com.cronelab.riscc.support.data.responses

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class FinishQuestionnaireApiResponse {
    @SerializedName("status")
    @Expose
    var status = false

    @SerializedName("message")
    @Expose
    var message=""
}