package com.cronelab.riscc.support.data.responses

import com.cronelab.riscc.support.data.database.table.DBLinks
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class LinksApiResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message=""

    @SerializedName("startPosition")
    @Expose
    var startPosition = 0

    @SerializedName("endPosition")
    @Expose
    var endPosition = 0

    @SerializedName("totalRecord")
    @Expose
    var totalRecord =0

    @SerializedName("totalPage")
    @Expose
    var totalPage = 0

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data {
        @SerializedName("list")
        @Expose
        var list: List<DBLinks>? = null
    }
}