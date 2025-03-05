package com.cronelab.riscc.ui.auth.pojo

import com.google.gson.annotations.SerializedName

data class UserSignUp(
    @SerializedName("devicePlatform") var devicePlatform: String = "",
    @SerializedName("deviceToken") var deviceToken: String = "",
    @SerializedName("firstName") var firstName: String = "",
    @SerializedName("lastName") var lastName: String = "",
    @SerializedName("emailAddress") var emailAddress: String = "",
    @SerializedName("password") var password: String = "",
    @SerializedName("mobileNumber") var mobileNumber: String = "",
    @SerializedName("image") var image: String = "",
    @SerializedName("verification") var userVerification: UserVerification = UserVerification()
)
