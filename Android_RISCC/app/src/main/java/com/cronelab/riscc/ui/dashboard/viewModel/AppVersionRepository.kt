package com.cronelab.riscc.ui.dashboard.viewModel

import com.cronelab.riscc.support.data.ApiFactory
import com.cronelab.riscc.support.data.SafeApiRequest
import com.cronelab.riscc.support.data.database.configuration.AppDatabase
import com.cronelab.riscc.support.data.database.table.DBAppVersion
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.manager.LanguageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppVersionRepository(
    val apiFactory: ApiFactory,
    val appDatabase: AppDatabase
) : SafeApiRequest() {

    class AppVersionResponse {
        var message = ""
        var error: Throwable? = null
        var appVersion: List<DBAppVersion> = ArrayList()
    }

    var appVersionResponse = AppVersionResponse()

    suspend fun getAppVersion(dbUser: DBUser): AppVersionResponse {
        return withContext(Dispatchers.IO) {
            requestAppVersion(dbUser)
            appVersionResponse
        }
    }

    private suspend fun requestAppVersion(dbUser: DBUser) {
        val appVersionResponse = AppVersionResponse()
        try {
            val apiResponse = apiRequest { apiFactory.getAppVersion(dbUser.token, LanguageManager.getLanguageCode()) }
            appVersionResponse.message = apiResponse.message
            if (apiResponse.status) {
                //appVersionResponse.appVersion = apiResponse.data?.appVersion!!
                val appVersionList = saveAppVersion(apiResponse.data?.appVersion!!)
                appVersionResponse.appVersion = appVersionList
                appVersionResponse.error = null
            } else {
                appVersionResponse.error = Throwable()
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
            appVersionResponse.error = ex
        }
        this.appVersionResponse = appVersionResponse
    }

    suspend fun saveAppVersion(appVersion: List<DBAppVersion>): List<DBAppVersion> {
        return withContext(Dispatchers.IO) {
            appDatabase.getDBAppVersionDao().insert(appVersion)
            appDatabase.getDBAppVersionDao().getAppVersion()
        }
    }

}