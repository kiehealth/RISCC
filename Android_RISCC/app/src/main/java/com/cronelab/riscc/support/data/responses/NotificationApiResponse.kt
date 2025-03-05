package com.cronelab.riscc.support.data.responses

import com.cronelab.riscc.support.data.database.table.DBNotification
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class NotificationApiResponse {
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
        @SerializedName("list")
        @Expose
        var notifications: List<DBNotification>? = null
    }
}