package com.cronelab.riscc.ui.auth.pojo

import com.google.gson.annotations.SerializedName

data class UserVerification(
    @SerializedName("id") var id: String = "",
    @SerializedName("verificationCode") var verificationCode: String = ""
)