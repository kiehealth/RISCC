package com.cronelab.riscc.support.data.responses

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class LogOutApiResponse {
    @SerializedName("message")
    @Expose
    var message=""

    @SerializedName("status")
    @Expose
    var status = false
}