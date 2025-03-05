package com.cronelab.riscc.support.analytics

import android.util.Log
import com.cronelab.riscc.support.common.utils.DateUtils
import com.cronelab.riscc.support.data.ApiFactory
import com.cronelab.riscc.support.data.SafeApiRequest
import com.cronelab.riscc.support.data.database.configuration.AppDatabase
import com.cronelab.riscc.support.data.database.table.DBAppAnalyticsType
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.manager.LanguageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.HashMap

class AppAnalyticsRepository(val apiFactory: ApiFactory, val appDatabase: AppDatabase) : SafeApiRequest() {

    val TAG = "AnalyticsFactory"

    suspend fun getAnalyticsType(dbUser: DBUser) {
        return withContext(Dispatchers.IO) {
            requestAnalyticsType(dbUser)
        }
    }

    /**
     * Get app Analytics Type and save into database
     */
    private suspend fun requestAnalyticsType(dbUser: DBUser) {
        try {
            val apiResponse = apiRequest { apiFactory.getAppAnalyticsType(dbUser.token, LanguageManager.getLanguageCode()) }
            if (apiResponse.status) {
                saveAppAnalytics(apiResponse.data!!.appAnalyticsTypes!!)
            } else {
                Log.e("AnalyticsFactory", "failed to get AppAnalytics")
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    suspend fun sendAnalytics(analyticsType: DBAppAnalyticsType, user: DBUser) {
        withContext(Dispatchers.IO) {
            postAnalytics(analyticsType, user)
        }
    }

    private suspend fun postAnalytics(analyticsType: DBAppAnalyticsType, user: DBUser) {
        try {
            val params = HashMap<String, String>()
            params["appAnalyticsKeyId"] = analyticsType.id.toString()
            params["description"] = analyticsType.description + ""
            val date = Calendar.getInstance().time
            params["value"] = DateUtils.getEpocTime(date).toString()
            val apiResponse = apiRequest { apiFactory.postAnalytics(user.token, LanguageManager.getLanguageCode(), params) }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private suspend fun saveAppAnalytics(appAnalyticsTypes: List<DBAppAnalyticsType>) {
        withContext(Dispatchers.IO) {
            appDatabase.getDBAppAnalyticsTypeDao().insert(appAnalyticsTypes)
        }
    }

    suspend fun getAppAnalytics(analyticsKey: String): List<DBAppAnalyticsType> {
        return withContext(Dispatchers.IO) {
            appDatabase.getDBAppAnalyticsTypeDao().getAppAnalyticsType(analyticsKey)
        }
    }


    suspend fun getAllAppAnalytics(): List<DBAppAnalyticsType> {
        return withContext(Dispatchers.IO) {
            appDatabase.getDBAppAnalyticsTypeDao().getAllAppAnalyticsType()
        }
    }


}