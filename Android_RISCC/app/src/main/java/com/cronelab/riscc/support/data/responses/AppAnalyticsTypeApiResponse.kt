package com.cronelab.riscc.support.data.responses

import com.cronelab.riscc.support.data.database.table.DBAppAnalyticsType
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AppAnalyticsTypeApiResponse {
    @SerializedName("status")
    @Expose
    var status = false

    @SerializedName("message")
    @Expose
    var message = ""

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data {
        @SerializedName("list")
        @Expose
        var appAnalyticsTypes: List<DBAppAnalyticsType>? = null
    }
}
