package com.cronelab.riscc.support.data.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class AnalyticsPostApiResponse {
    @SerializedName("status")
    @Expose
    var status = false

    @SerializedName("message")
    @Expose
    var message = ""

   /* @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data {
        @SerializedName("appAnalytics")
        @Expose
        var appAnalytics: AppAnalytics? = null
    }

    class AppAnalytics {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("value")
        @Expose
        var value: String? = null

        @SerializedName("appAnalyticsKey")
        @Expose
        var appAnalyticsKey: AppAnalyticsKey? = null

        @SerializedName("user")
        @Expose
        var user: User? = null
    }

    class AppAnalyticsKey {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("appAnalyticsKeyDataType")
        @Expose
        var appAnalyticsKeyDataType: String? = null
    }


    class User {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("firstName")
        @Expose
        var firstName: String? = null

        @SerializedName("lastName")
        @Expose
        var lastName: String? = null

        @SerializedName("mobileNumber")
        @Expose
        var mobileNumber: String? = null

        @SerializedName("emailAddress")
        @Expose
        var emailAddress: String? = null

        @SerializedName("imageUrl")
        @Expose
        var imageUrl: String? = null

        @SerializedName("hasAnsweredQuestionnaire")
        @Expose
        var hasAnsweredQuestionnaire: Boolean? = null
    }*/
}