package com.cronelab.riscc.ui.links.model

import android.util.Log
import com.cronelab.riscc.support.data.ApiFactory
import com.cronelab.riscc.support.data.SafeApiRequest
import com.cronelab.riscc.support.data.database.configuration.AppDatabase
import com.cronelab.riscc.support.data.database.table.DBLinks
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.helper.Coroutines
import com.cronelab.riscc.support.manager.LanguageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class LinksRepository(
    private val apiFactory: ApiFactory,
    private val appDatabase: AppDatabase
) : SafeApiRequest() {
    private val TAG = "LinksRepository"

    class LinksResponse {
        var links: List<DBLinks> = ArrayList()
        var error: Throwable? = Throwable()
        var message: String? = ""
        var totalPage = 0
        var endPosition = 0
        var totalRecord = 0
    }

    private var linksResponse = LinksResponse()

    suspend fun getLinks(dbUser: DBUser, page: Int, size: Int): LinksResponse {
        return withContext(Dispatchers.IO) {
            getLinksFromServer(dbUser, page, size)
            linksResponse
        }
    }

    private suspend fun getLinksFromServer(dbUser: DBUser, page: Int, size: Int) {
        val linksResponse = LinksResponse()
        try {
            val apiResponse = apiRequest { apiFactory.getLinks(dbUser.token, LanguageManager.getLanguageCode(), page, size) }
            if (apiResponse.status!!) {
                linksResponse.links = apiResponse.data!!.list!!
                linksResponse.error = null
                linksResponse.totalPage = apiResponse.totalPage
                linksResponse.endPosition = apiResponse.endPosition
                linksResponse.totalRecord = apiResponse.totalRecord
                saveLinks(linksResponse.links)
            } else {
                linksResponse.error = Throwable()
                linksResponse.message = apiResponse.message
            }
            Log.d(TAG, "links size: " + linksResponse.links.size)
        } catch (ex: Exception) {
            ex.printStackTrace()
            linksResponse.error = ex
            linksResponse.message = "Unable to get data"
        }
        this.linksResponse = linksResponse
    }

    private fun saveLinks(links: List<DBLinks>) {
        Coroutines.io {
            appDatabase.getDBLinksDao().insert(links)
        }
    }

    suspend fun getLinksFromDB(): List<DBLinks>? {
        return withContext(Dispatchers.IO) {
            appDatabase.getDBLinksDao().getAllLinks()
        }
    }

    suspend fun searchMatchingLinks(searchTerms: String) {
        return withContext(Dispatchers.IO) {
            appDatabase.getDBLinksDao().getMatchingLinks(searchTerms)
        }
    }

}