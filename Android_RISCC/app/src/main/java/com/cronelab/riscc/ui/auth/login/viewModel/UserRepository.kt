package com.cronelab.riscc.ui.auth.login.viewModel

import android.util.Log
import com.cronelab.riscc.R
import com.cronelab.riscc.support.AppController
import com.cronelab.riscc.support.constants.Constants
import com.cronelab.riscc.support.constants.Constants.DEVICE_PLATFORM
import com.cronelab.riscc.support.data.ApiFactory
import com.cronelab.riscc.support.data.SafeApiRequest
import com.cronelab.riscc.support.data.database.configuration.AppDatabase
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.helper.Coroutines
import com.cronelab.riscc.support.helper.DeviceTokenManager
import com.cronelab.riscc.support.manager.LanguageManager
import com.cronelab.riscc.ui.auth.pojo.Verification
import com.cronelab.riscc.ui.auth.pojo.UserSignUp
import com.cronelab.riscc.ui.auth.pojo.UserVerification
import com.cronelab.riscc.ui.auth.responses.*
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UserRepository(
    private val apiFactory: ApiFactory,
    private val appDatabase: AppDatabase
) : SafeApiRequest() {
    val TAG = "UserRepository"
    private var response = UserResponse()
    private var verificationCodeResponse = VerificationCodeResponse()
    private var signUpResponse = SignUpResponse()
    private var passwordResetCodeResponse = PasswordResetCodeResponse()
    private var passwordResetResponse = PasswordResetResponse()
    private var changePasswordResponse = ChangePasswordResponse()
    private var updateProfileResponse = UserResponse()
    private var logOutResponse = LogOutResponse()

    suspend fun getVerificationCode(user: DBUser): VerificationCodeResponse {
        return withContext(Dispatchers.IO) {
            requestVerificationCode(user)
            verificationCodeResponse
        }
    }

    private suspend fun requestVerificationCode(user: DBUser) {
        val verificationCodeResponse = VerificationCodeResponse()
        val params: HashMap<String, String> = HashMap()
        params["emailAddress"] = user.emailAddress
        //params["mobileNumber"] = user.mobileNumber
        try {
            val apiResponse = apiRequest { apiFactory.getVerification(params, LanguageManager.getLanguageCode()) }
            if (apiResponse.status) {
                verificationCodeResponse.verification = apiResponse.data!!.verification!!
                verificationCodeResponse.error = null
            } else {
                verificationCodeResponse.error = Throwable()
                verificationCodeResponse.message = apiResponse.message
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            verificationCodeResponse.error = ex
            verificationCodeResponse.message = "Unable to get data"
        }
        this.verificationCodeResponse = verificationCodeResponse
    }

    suspend fun login(email: String, password: String): UserResponse {
        return withContext(Dispatchers.IO) {
            performLogin(email, password)
            response
        }
    }

    private suspend fun performLogin(email: String, password: String) {
        val map: HashMap<String, String> = HashMap()
        map["username"] = email
        map["password"] = password
        map["devicePlatform"] = "ANDROID"
        map["deviceToken"] = DeviceTokenManager().getToken(AppController.instance.applicationContext)
        Log.d(TAG, "params: ${Gson().toJson(map)}")

        val userRepositoryModel = UserResponse()
        try {
            val apiResponse = apiRequest { apiFactory.login(map, LanguageManager.getLanguageCode()) }
            if (apiResponse.status) {
                userRepositoryModel.user = apiResponse.data!!.user!!
                userRepositoryModel.error = null
                userRepositoryModel.user.token = apiResponse.data!!.token!!
                saveUser(userRepositoryModel.user)
            } else {
                userRepositoryModel.error = Throwable()
                userRepositoryModel.message = apiResponse.message
            }

        } catch (e: Exception) {
            Log.e("User Repository", "ex: " + e.message)
            userRepositoryModel.error = e
            userRepositoryModel.message = e.message + ""
        }
        response = userRepositoryModel
    }

    suspend fun signUp(user: DBUser, verification: Verification): SignUpResponse {
        return withContext(Dispatchers.IO) {
            performSignUp(user, verification)
            signUpResponse
        }
    }

    private suspend fun performSignUp(user: DBUser, verification: Verification) {
        val userVerification = UserVerification(verification.id.toString(), verification.verificationCode.toString())
        val userSignUpObj = UserSignUp(
            Constants.DEVICE_PLATFORM,
            DeviceTokenManager().getToken(AppController.instance.applicationContext),
            user.firstName, user.lastName, user.emailAddress, user.password,
            user.mobileNumber, user.imageEncoded, userVerification
        )

        val signUpResponse = SignUpResponse()
        try {
            val apiResponse = apiRequest { apiFactory.signUp(userSignUpObj, LanguageManager.getLanguageCode()) }
            if (apiResponse.status) {
                signUpResponse.user = apiResponse.data!!.user!!
                signUpResponse.error = null
            } else {
                signUpResponse.error = Throwable()
                signUpResponse.message = apiResponse.message
            }
        } catch (e: Exception) {
            Log.e("User Repository", "ex: " + e.message)
        }
        this.signUpResponse = signUpResponse
    }

    suspend fun getPasswordResetCode(email: String): PasswordResetCodeResponse {
        return withContext(Dispatchers.IO) {
            getPasswordResetCodeFromServer(email)
            passwordResetCodeResponse
        }
    }

    private suspend fun getPasswordResetCodeFromServer(email: String) {
        val passwordResetCodeResponse = PasswordResetCodeResponse()
        try {
            val apiResponse = apiRequest { apiFactory.getPasswordResetCode(email, LanguageManager.getLanguageCode()) }
            passwordResetCodeResponse.message = apiResponse.message!!
            if (apiResponse.status) {
                passwordResetCodeResponse.error = null
            } else {
                passwordResetCodeResponse.error = Throwable()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            passwordResetCodeResponse.error = ex
            val context = AppController.instance.applicationContext
            passwordResetCodeResponse.message = context.getString(R.string.unableToRequestPasswordResetCode)
        }
        this.passwordResetCodeResponse = passwordResetCodeResponse
    }

    suspend fun resetPassword(email: String, newPassword: String, verificationCode: String): PasswordResetResponse {
        return withContext(Dispatchers.IO) {
            performResetPassword(email, newPassword, verificationCode)
            passwordResetResponse
        }
    }

    private suspend fun performResetPassword(email: String, newPassword: String, verificationCode: String) {
        val passwordResetResponse = PasswordResetResponse()
        try {
            val params = HashMap<String, String>()
            params["emailAddress"] = email
            params["newPassword"] = newPassword
            params["verificationCode"] = verificationCode
            val apiResponse = apiRequest { apiFactory.resetPassword(params, LanguageManager.getLanguageCode()) }
            passwordResetResponse.message = apiResponse.message
            if (apiResponse.status) {
                passwordResetResponse.error = null
            } else {
                passwordResetResponse.error = Throwable()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            passwordResetResponse.error = ex
            val context = AppController.instance.applicationContext
            passwordResetResponse.message = context.getString(R.string.unableToResetPassword)
        }
        this.passwordResetResponse = passwordResetResponse
    }

    suspend fun changePassword(dbUser: DBUser, oldPassword: String, newPassword: String): ChangePasswordResponse {
        return withContext(Dispatchers.IO) {
            requestChangePassword(dbUser, oldPassword, newPassword)
            changePasswordResponse
        }
    }

    private suspend fun requestChangePassword(dbUser: DBUser, oldPassword: String, newPassword: String) {
        val changePasswordResponse = ChangePasswordResponse()
        try {
            val bodyParams = HashMap<String, String>()
            bodyParams["emailAddress"] = dbUser.emailAddress
            bodyParams["oldPassword"] = oldPassword
            bodyParams["newPassword"] = newPassword
            val apiResponse = apiRequest { apiFactory.changePassword(dbUser.token, LanguageManager.getLanguageCode(), bodyParams) }
            changePasswordResponse.message = apiResponse.message
            if (apiResponse.status) {
                changePasswordResponse.error = null
            } else {
                changePasswordResponse.error = Throwable()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            changePasswordResponse.error = Throwable()
            changePasswordResponse.message = AppController.instance.applicationContext.getString(R.string.unableToChangePassword)
        }
        this.changePasswordResponse = changePasswordResponse
    }


    suspend fun updateProfile(user: DBUser): UserResponse {
        return withContext(Dispatchers.IO) {
            requestUpdateProfile(user)
            updateProfileResponse
        }
    }

    private suspend fun requestUpdateProfile(user: DBUser) {
        val updateProfileResponse = UserResponse()
        try {
            val bodyParams = HashMap<String, String>()
            bodyParams["devicePlatform"] = DEVICE_PLATFORM
            bodyParams["deviceToken"] = DeviceTokenManager().getToken(AppController.instance.applicationContext)
            bodyParams["image"] = user.imageEncoded
            bodyParams["firstName"] = user.firstName
            bodyParams["lastName"] = user.lastName
            bodyParams["emailAddress"] = user.emailAddress
            bodyParams["mobileNumber"] = user.mobileNumber

            val apiResponse = apiRequest { apiFactory.updateProfile(user.token, LanguageManager.getLanguageCode(), bodyParams) }
            updateProfileResponse.message = apiResponse.message
            if (apiResponse.status) {
                updateProfileResponse.user = apiResponse.data!!.user!!
                updateProfileResponse.error = null
                updateProfileResponse.user.token = user.token
                saveUser(updateProfileResponse.user)
            } else {
                updateProfileResponse.error = Throwable()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            updateProfileResponse.error = Throwable()
            updateProfileResponse.message = AppController.instance.applicationContext.getString(R.string.unableToUpdateProfile)
        }
        this.updateProfileResponse = updateProfileResponse
    }

    suspend fun updateFcmToken(user: DBUser) {
        return withContext(Dispatchers.IO) {
            requestUpdateFcmToken(user)
            //updateProfileResponse
        }
    }

    //update profile api is also used to update fcm token
    //but only sending the required keys only
    private suspend fun requestUpdateFcmToken(user: DBUser) {
        val updateProfileResponse = UserResponse()
        try {
            val bodyParams = HashMap<String, String>()
            bodyParams["devicePlatform"] = DEVICE_PLATFORM
            bodyParams["deviceToken"] = DeviceTokenManager().getToken(AppController.instance.applicationContext)

            val apiResponse = apiRequest { apiFactory.updateProfile(user.token, LanguageManager.getLanguageCode(), bodyParams) }
            updateProfileResponse.message = apiResponse.message
            if (apiResponse.status) {
                updateProfileResponse.user = apiResponse.data!!.user!!
                updateProfileResponse.error = null
                updateProfileResponse.user.token = user.token
                saveUser(updateProfileResponse.user)
            } else {
                updateProfileResponse.error = Throwable()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            updateProfileResponse.error = Throwable()
            updateProfileResponse.message = AppController.instance.applicationContext.getString(R.string.unableToUpdateProfile)
        }
        //this.updateProfileResponse = updateProfileResponse
    }


    private suspend fun saveUser(user: DBUser) {
        withContext(Dispatchers.IO)  {
            appDatabase.getDBUserDao().deleteAll()
            appDatabase.getDBUserDao().insert(user)
        }
    }

    suspend fun getUserFromDB(): List<DBUser>? {
        return withContext(Dispatchers.IO) {
            appDatabase.getDBUserDao().getAllUsers()
        }
    }

    suspend fun logout(dbUser: DBUser): LogOutResponse {
        return withContext(Dispatchers.IO) {
            requestLogout(dbUser)
            logOutResponse
        }
    }

    private suspend fun requestLogout(dbUser: DBUser) {
        val logOutResponse = LogOutResponse()
        try {
            val apiResponse = apiRequest { apiFactory.logOut(dbUser.token, LanguageManager.getLanguageCode()) }
            logOutResponse.message = apiResponse.message
            if (apiResponse.status) {
                logOutResponse.error = null
            } else {
                logOutResponse.error = Throwable()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            logOutResponse.error = ex
            logOutResponse.message = AppController.instance.applicationContext.getString(R.string.unableToLogOut)
        }
        this.logOutResponse = logOutResponse
    }


}