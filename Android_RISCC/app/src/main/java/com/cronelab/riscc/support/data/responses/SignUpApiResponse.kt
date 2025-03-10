package com.cronelab.riscc.support.data.responses

import com.cronelab.riscc.support.data.database.table.DBUser
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class SignUpApiResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean=false

    @SerializedName("message")
    @Expose
    var message=""

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data {
        @SerializedName("user")
        @Expose
        var user: DBUser? = null
    }
}