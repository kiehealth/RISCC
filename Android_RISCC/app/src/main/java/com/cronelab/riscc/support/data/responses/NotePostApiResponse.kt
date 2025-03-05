package com.cronelab.riscc.support.data.responses

import com.cronelab.riscc.support.data.database.table.DBNote
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotePostApiResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean = false

    @SerializedName("message")
    @Expose
    var message: String = ""

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data {
        @SerializedName("note")
        @Expose
        var note: DBNote? = null
    }
}