package com.cronelab.riscc.support.data.responses

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class NoteDeleteApiResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean=false

    @SerializedName("message")
    @Expose
    var message: String=""
}