package com.cronelab.riscc.support.data.responses

import com.cronelab.riscc.ui.auth.pojo.Verification
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class VerificationApiResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean = false

    @SerializedName("message")
    @Expose
    var message: String=""

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data {
        @SerializedName("verification")
        @Expose
        var verification: Verification? = null
    }

}
