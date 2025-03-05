package com.cronelab.riscc.ui.auth.login.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.ui.auth.pojo.Verification
import com.cronelab.riscc.ui.auth.responses.*
import kotlinx.coroutines.launch


class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    val TAG = "LoginViewModel"

    var loginResponse = MutableLiveData<UserResponse>()
    var verificationCodeResponse = MutableLiveData<VerificationCodeResponse>()
    var signUpResponse = MutableLiveData<SignUpResponse>()
    var passwordResetCodeResponse = MutableLiveData<PasswordResetCodeResponse>()
    var passwordResetResponse = MutableLiveData<PasswordResetResponse>()
    var changePasswordResponse = MutableLiveData<ChangePasswordResponse>()
    var updateProfileResponse = MutableLiveData<UserResponse>()
    var logOutResponse = MutableLiveData<LogOutResponse>()

    var user = MutableLiveData<List<DBUser>>()


    fun getVerificationCode(user: DBUser) {
        viewModelScope.launch {
            try {
                verificationCodeResponse.postValue(userRepository.getVerificationCode(user))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                loginResponse.postValue(userRepository.login(email, password))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun signUp(user: DBUser, verification: Verification) {
        viewModelScope.launch {
            try {
                signUpResponse.postValue(userRepository.signUp(user, verification))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getPasswordResetCode(email: String) {
        viewModelScope.launch {
            try {
                passwordResetCodeResponse.postValue(userRepository.getPasswordResetCode(email))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun resetPassword(email: String, newPassword: String, verificationCode: String) {
        viewModelScope.launch {
            try {
                passwordResetResponse.postValue(userRepository.resetPassword(email, newPassword, verificationCode))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun changePassword(dbUser: DBUser, oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            try {
                changePasswordResponse.postValue(userRepository.changePassword(dbUser, oldPassword, newPassword))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun getUser() {
        viewModelScope.launch {
            try {
                user.postValue(userRepository.getUserFromDB())
            } catch (ex: Exception) {
                Log.e(TAG, "ex: ${ex.message}")
            }
        }
    }

    fun updateProfile(user: DBUser) {
        viewModelScope.launch {
            try {
                updateProfileResponse.postValue(userRepository.updateProfile(user))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
    fun updateFcmToken(user: DBUser) {
        viewModelScope.launch {
            try {
                userRepository.updateFcmToken(user)
//                updateProfileResponse.postValue(userRepository.updateFcmToken(user))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }


    fun logOut(dbUser: DBUser) {
        viewModelScope.launch {
            try {
                logOutResponse.postValue(userRepository.logout(dbUser))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}