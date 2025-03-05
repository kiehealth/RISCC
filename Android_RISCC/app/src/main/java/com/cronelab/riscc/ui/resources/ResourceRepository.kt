package com.cronelab.riscc.ui.resources;

import android.util.Log
import com.cronelab.riscc.R
import com.cronelab.riscc.support.AppController
import com.cronelab.riscc.support.data.ApiFactory;
import com.cronelab.riscc.support.data.SafeApiRequest
import com.cronelab.riscc.support.data.database.configuration.AppDatabase;
import com.cronelab.riscc.support.data.database.table.DBResourceFile
import com.cronelab.riscc.support.manager.LanguageManager
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResourceRepository(
    val apiFactory: ApiFactory,
    val appDatabase: AppDatabase
) : SafeApiRequest() {
    val TAG = "ResourceRepository"

    class ResourceResponse {
        var message = ""
        var error: Throwable? = null
        var resourcesFileList: List<DBResourceFile> = listOf()
    }

    var resourceResponse = ResourceResponse()

    suspend fun getResourcesData(): ResourceResponse {
        return withContext(Dispatchers.IO) {
            requestResourcesData()
            resourceResponse
        }
    }

    /*suspend fun getResourcesFileFromDB(): ResourceResponse {
        return withContext(Dispatchers.IO) {
            requestResourcesFileFromDB()
            resourceResponse
        }
    }*/

    private suspend fun requestResourcesData() {
        val resourceResponse = ResourceResponse()
        try {
            val apiResponse = apiRequest { apiFactory.getResources(LanguageManager.getLanguageCode()) }
            resourceResponse.message = apiResponse.message
            if (apiResponse.status) {
                if (apiResponse.data != null) {
                    val resourcesFileList = saveResourceFiles(apiResponse.data!!.resourceFiles!!)
                    resourceResponse.resourcesFileList = resourcesFileList
                }
                resourceResponse.error = null
            } else {
                resourceResponse.error = Throwable()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            resourceResponse.error = Throwable()
            resourceResponse.message = AppController.instance.applicationContext.getString(R.string.unableToGetData)
        }
        this.resourceResponse = resourceResponse
    }

    private suspend fun saveResourceFiles(resourcesFile: List<DBResourceFile>): List<DBResourceFile> {
        return withContext(Dispatchers.IO) {
            appDatabase.getDBResourceFilesDao().insert(resourcesFile)
            appDatabase.getDBResourceFilesDao().getResourceFiles()
        }
    }

    suspend fun getResourcesFileFromDB(): List<DBResourceFile> {
        return withContext(Dispatchers.IO) {
            appDatabase.getDBResourceFilesDao().getResourceFiles()
        }
    }
    suspend fun getResourcesUrlFromDB(title:String): DBResourceFile{
        return withContext(Dispatchers.IO) {
            appDatabase.getDBResourceFilesDao().getResourceFile(title)
        }
    }

}
