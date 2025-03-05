package com.cronelab.riscc.support.helper

import android.content.Context
import androidx.preference.PreferenceManager
import com.cronelab.riscc.support.constants.SharedPreferenceConstants.USER_TOKEN

class DeviceTokenManager {
     fun getToken(context: Context): String {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val token = sharedPrefs.getString(USER_TOKEN, "")
        return token ?: ""
    }

     fun saveToken(token: String, context: Context) {
        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
        with(sharedPreference.edit()) {
            putString(USER_TOKEN, token)
            commit()
        }
    }
}