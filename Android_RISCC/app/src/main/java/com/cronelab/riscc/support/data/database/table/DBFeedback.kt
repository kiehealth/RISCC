package com.cronelab.riscc.support.data.database.table

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DBFeedback :Serializable{
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("date")
    @Expose
    var date: String? = null

  /*  @SerializedName("feedbackBy")
    @Expose
    var feedbackBy: FeedbackBy? = null*/
}