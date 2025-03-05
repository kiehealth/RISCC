package com.cronelab.riscc.ui.auth.pojo

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Verification: Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("emailAddress")
    @Expose
    var emailAddress: String? = null

    @SerializedName("mobileNumber")
    @Expose
    var mobileNumber: String? = null

    @SerializedName("verificationCode")
    @Expose
    var verificationCode: String? = null

    @SerializedName("emailSent")
    @Expose
    var emailSent: Boolean? = null

    @SerializedName("smsSent")
    @Expose
    var smsSent: Boolean? = null
}