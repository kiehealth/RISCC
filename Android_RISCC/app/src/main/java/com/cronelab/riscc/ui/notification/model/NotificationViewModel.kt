package com.cronelab.riscc.ui.notification.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cronelab.riscc.support.data.database.table.DBNotification
import com.cronelab.riscc.support.data.database.table.DBUser
import kotlinx.coroutines.launch
import java.lang.Exception

class NotificationViewModel(private val notificationRepository: NotificationRepository) : ViewModel() {
    val TAG = "NotificationViewModel"
    var notificationResponse = MutableLiveData<NotificationRepository.NotificationResponse>()
    var notificationList = MutableLiveData<List<DBNotification>>()

    fun getNotificationFromServer(dbUser: DBUser) {
        viewModelScope.launch {
            try {
                notificationResponse.postValue(notificationRepository.getNotifications(dbUser))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun getNotificationFromDB() {
        notificationList = MutableLiveData<List<DBNotification>>()
        viewModelScope.launch {
            try {
                notificationList.postValue(notificationRepository.getNotificationsFromDB())
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}