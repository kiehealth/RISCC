package com.cronelab.riscc.support.data.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class FeedbackApiResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean=false

    @SerializedName("message")
    @Expose
    var message: String=""

    /*@SerializedName("data")
    @Expose
    var data: Data? = null

    class Data {
        @SerializedName("feedback")
        @Expose
        var feedback: DBFeedback? = null
    }*/
}