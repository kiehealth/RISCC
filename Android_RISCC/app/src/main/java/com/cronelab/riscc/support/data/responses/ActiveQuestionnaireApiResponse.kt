package com.cronelab.riscc.support.data.responses

import com.cronelab.riscc.support.data.database.table.questions.DBActiveQuestionnaireGroup
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ActiveQuestionnaireApiResponse {
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
        @SerializedName("groupQuestionnaires")
        @Expose
        var activeQuestionnaireGroups: List<DBActiveQuestionnaireGroup>? = null
    }
}