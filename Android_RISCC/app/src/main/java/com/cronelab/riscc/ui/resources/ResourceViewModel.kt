package com.cronelab.riscc.ui.resources

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cronelab.riscc.support.data.database.table.DBResourceFile
import kotlinx.coroutines.launch

class ResourceViewModel(var repository: ResourceRepository) : ViewModel() {
    var resourceData = MutableLiveData<ResourceRepository.ResourceResponse>()
    var resourcesList = MutableLiveData<List<DBResourceFile>>()
    var resourceUrl = MutableLiveData<DBResourceFile>()

    fun getResourceFiles() {
        viewModelScope.launch {
            try {
                resourceData.postValue(repository.getResourcesData())
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun getResourceFilesFromDB() {
        viewModelScope.launch {
            try {
                resourcesList.postValue(repository.getResourcesFileFromDB())
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
    fun getResourceUrlFromDB(title:String) {
        viewModelScope.launch {
            try {
                resourceUrl.postValue(repository.getResourcesUrlFromDB(title))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}