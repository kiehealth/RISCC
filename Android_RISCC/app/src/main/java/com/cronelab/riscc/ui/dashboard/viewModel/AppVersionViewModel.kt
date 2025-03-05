package com.cronelab.riscc.ui.dashboard.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cronelab.riscc.support.data.database.table.DBUser
import kotlinx.coroutines.launch
import java.lang.Exception


class AppVersionViewModel(var repository: AppVersionRepository) : ViewModel() {
    var appVersionResponse = MutableLiveData<AppVersionRepository.AppVersionResponse>()

    fun getAppVersion(dbUser: DBUser) {
        viewModelScope.launch {
            try {
                appVersionResponse.postValue(repository.getAppVersion(dbUser))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

    }
}