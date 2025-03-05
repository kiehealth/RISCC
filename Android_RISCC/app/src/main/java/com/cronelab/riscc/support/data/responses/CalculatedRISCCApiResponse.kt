package com.cronelab.riscc.support.data.responses

import com.cronelab.riscc.support.data.database.table.questions.DBQuestions
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CalculatedRISCCApiResponse {

    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("data")
    @Expose
    var data: Data? = null
    @SerializedName("status")
    @Expose
    var status: Boolean=false

    class Data {
        @SerializedName("risccValue")
        @Expose
        var risccValue: List<RisccCalculated>? = null
    }

    class RisccCalculated {
        @SerializedName("message")
        @Expose
        var message: String? = null
        @SerializedName("risccValue")
        @Expose
        var risccValue: Int? = null
        @SerializedName("calculationType")
        @Expose
        var calculationType: String? = null

        @SerializedName("moreInfo")
        @Expose
        var moreInfo: String? = null

    }


}


