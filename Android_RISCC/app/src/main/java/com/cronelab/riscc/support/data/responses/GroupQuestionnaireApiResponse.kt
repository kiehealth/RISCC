package com.cronelab.riscc.support.data.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GroupQuestionnaireApiResponse {
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
        var groupQuestionnaireList: List<GroupQuestionnaire>? = null
    }

    class GroupQuestionnaire {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("startDateTime")
        @Expose
        var startDateTime: String? = null

        @SerializedName("endDateTime")
        @Expose
        var endDateTime: String? = null

        @SerializedName("reminderMessage")
        @Expose
        var reminderMessage: String? = null

        @SerializedName("reminderTimeInterval")
        @Expose
        var reminderTimeInterval: Int? = null

        @SerializedName("questionnaire")
        @Expose
        var questionnaire: Questionnaire? = null
    }

    class Questionnaire {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("title")
        @Expose
        var title=""
    }
}