package com.cronelab.riscc.ui.aboutUs

import android.util.Log
import com.cronelab.riscc.support.data.ApiFactory
import com.cronelab.riscc.support.data.SafeApiRequest
import com.cronelab.riscc.support.data.database.configuration.AppDatabase
import com.cronelab.riscc.support.data.database.table.DBAboutUs
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.manager.LanguageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class AboutUsRepository(
    val apiFactory: ApiFactory,
    val appDatabase: AppDatabase
) : SafeApiRequest() {

    val TAG = "AboutUsRepository"

    class AboutUsResponse {
        var message = ""
        var error: Throwable? = null
        var aboutUs: List<DBAboutUs> = listOf()
    }

    var aboutUsResponse = AboutUsResponse()

    suspend fun getAboutUsData(dbUser: DBUser): AboutUsResponse {
        return withContext(Dispatchers.IO) {
            requestAboutData(dbUser)
            aboutUsResponse
        }
    }


    private suspend fun requestAboutData(dbUser: DBUser) {
        val aboutUsResponse = AboutUsResponse()
        try {
            val apiResponse = apiRequest {
                apiFactory.getAboutData(dbUser.token, LanguageManager.getLanguageCode())
            }
            aboutUsResponse.message = apiResponse.message
            if (apiResponse.status) {
                aboutUsResponse.error = null
                val aboutUsList = apiResponse.data!!.aboutUs!!
                aboutUsResponse.aboutUs = saveAboutUsData(aboutUsList)
                Log.w(TAG, "about name: ${aboutUsResponse.aboutUs[0].name}")
            } else {
                aboutUsResponse.error = Throwable()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            aboutUsResponse.error = ex
        }
        this.aboutUsResponse = aboutUsResponse
    }

    private suspend fun saveAboutUsData(aboutUs: DBAboutUs): List<DBAboutUs> {
        return withContext(Dispatchers.IO) {
            appDatabase.getDBAboutUsDao().insert(aboutUs)
            appDatabase.getDBAboutUsDao().getAboutUs()
        }
    }
}