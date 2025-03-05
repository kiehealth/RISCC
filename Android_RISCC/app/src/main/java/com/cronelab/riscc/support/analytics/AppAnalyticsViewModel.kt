package com.cronelab.riscc.support.analytics

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cronelab.riscc.support.data.database.table.DBAppAnalyticsType
import com.cronelab.riscc.support.data.database.table.DBUser
import kotlinx.coroutines.launch

class AppAnalyticsViewModel(val repository: AppAnalyticsRepository) : ViewModel() {

    val TAG = "AppAnalyticsViewModel"

    val appAnalyticsType = MutableLiveData<List<DBAppAnalyticsType>?>()
    val allAppAnalyticsType = MutableLiveData<List<DBAppAnalyticsType>>()

    fun getAppAnalytics(user: DBUser) {
        viewModelScope.launch {
            try {
                repository.getAnalyticsType(user)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun sendAppAnalytics(analyticsType: DBAppAnalyticsType, user: DBUser) {
        viewModelScope.launch {
            try {
                repository.sendAnalytics(analyticsType, user)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun getAllAppAnalytics() {
        viewModelScope.launch {
            try {
                allAppAnalyticsType.postValue(repository.getAllAppAnalytics())
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            repository.getAllAppAnalytics()
        }
    }


    fun getAppAnalytics(key: String) {
        viewModelScope.launch {
            try {
                appAnalyticsType.postValue(repository.getAppAnalytics(key))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun resetObserver(){
        appAnalyticsType.value = null
    }
}