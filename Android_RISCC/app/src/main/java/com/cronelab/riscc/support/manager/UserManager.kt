package com.cronelab.riscc.support.manager

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import com.cronelab.riscc.support.AppController
import com.cronelab.riscc.support.constants.SharedPreferenceConstants
import com.cronelab.riscc.support.data.database.table.DBUser
import com.google.gson.Gson

class UserManager {

    val TAG = "UserManager"

    fun saveAuth(email: String, password: String) {
        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(AppController.instance.applicationContext)
        with(sharedPreference.edit()) {
            putString(SharedPreferenceConstants.USER_EMAIL, email)
            putString(SharedPreferenceConstants.USER_PASSWORD, password)
            commit()
        }
    }

    fun getUserEmail(): String? {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(AppController.instance.applicationContext)
        return sharedPrefs.getString(SharedPreferenceConstants.USER_EMAIL, "")
    }

    fun getUserPassword(): String? {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(AppController.instance.applicationContext)
        return sharedPrefs.getString(SharedPreferenceConstants.USER_PASSWORD, "")
    }

    fun saveUser(context: Context, user: DBUser) {
        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
        with(sharedPreference.edit()) {
            putString(SharedPreferenceConstants.USER, Gson().toJson(user))
            commit()
        }
    }

    fun getUser(context: Context): DBUser? {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val userString = sharedPrefs.getString(SharedPreferenceConstants.USER, "")
        val user: DBUser
        try {
            user = Gson().fromJson(userString, DBUser::class.java)
            return user
        } catch (ex: Exception) {
            Log.w(TAG, "exception on getting user")
            ex.printStackTrace()
            return null
        }
    }

    fun removeUser(context: Context) {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        with(sharedPrefs.edit()) {
            remove(SharedPreferenceConstants.USER)
            commit()
        }
    }
}