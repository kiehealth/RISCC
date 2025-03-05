package com.cronelab.riscc.ui.notification.model

import android.util.Log
import com.cronelab.riscc.support.data.ApiFactory
import com.cronelab.riscc.support.data.SafeApiRequest
import com.cronelab.riscc.support.data.database.configuration.AppDatabase
import com.cronelab.riscc.support.data.database.table.DBNotification
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.helper.Coroutines
import com.cronelab.riscc.support.manager.LanguageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationRepository(
    private val apiFactory: ApiFactory,
    private val appDatabase: AppDatabase
) : SafeApiRequest() {
    val TAG = "NotificationRepository"

    class NotificationResponse {
        var notificationList: List<DBNotification> = ArrayList()
        var error: Throwable? = Throwable()
        var message: String? = ""
    }

    private var notificationResponse = NotificationResponse()

    suspend fun getNotifications(dbUser: DBUser): NotificationResponse {
        return withContext(Dispatchers.IO) {
            getNotificationsFromServer(dbUser)
            notificationResponse
        }
    }

    private suspend fun getNotificationsFromServer(dbUser: DBUser) {
        val notificationResponse = NotificationResponse()
        try {
            val apiResponse = apiRequest { apiFactory.getNotification(dbUser.token, LanguageManager.getLanguageCode()) }
            if (apiResponse.status!!) {
                notificationResponse.notificationList = apiResponse.data!!.notifications!!
                notificationResponse.error = null
                saveNotifications(notificationResponse.notificationList)
            } else {
                notificationResponse.error = Throwable()
                notificationResponse.message = apiResponse.message
            }
            Log.d(TAG, "Notification size: " + notificationResponse.notificationList.size)
        } catch (ex: Exception) {
            ex.printStackTrace()
            notificationResponse.error = ex
            notificationResponse.message = "Unable to get data"
        }
        this.notificationResponse = notificationResponse
    }

    private fun saveNotifications(notificationList: List<DBNotification>) {
        Coroutines.io {
            appDatabase.getDBNotificationDao().insert(notificationList)
        }
    }

    suspend fun getNotificationsFromDB(): List<DBNotification>? {
        return withContext(Dispatchers.IO) {
            appDatabase.getDBNotificationDao().getAllNotification()
        }
    }
}