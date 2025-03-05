package com.cronelab.riscc.support.data.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NoteUpdateApiResponse {
    @SerializedName("status")
    @Expose
    var status=false

    @SerializedName("message")
    @Expose
    var message=""

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data {
        @SerializedName("note")
        @Expose
        var note: Note? = null
    }

    class Note {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("createdDateTime")
        @Expose
        var createdDateTime: String? = null
    }
}