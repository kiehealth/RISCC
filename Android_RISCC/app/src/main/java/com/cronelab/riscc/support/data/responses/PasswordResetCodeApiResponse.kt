package com.cronelab.riscc.support.data.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class PasswordResetCodeApiResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean = false

    @SerializedName("message")
    @Expose
    var message=""

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data {
        @SerializedName("verificationCode")
        @Expose
        var verificationCode: String? = null
    }
}