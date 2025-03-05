package com.cronelab.riscc.ui.links.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cronelab.riscc.support.data.database.table.DBLinks
import com.cronelab.riscc.support.data.database.table.DBUser
import kotlinx.coroutines.launch
import java.lang.Exception

class LinksViewModel(private val linksRepository: LinksRepository) : ViewModel() {

    val TAG = "LinksViewModel"

    var linksResponse = MutableLiveData<LinksRepository.LinksResponse>()
    var linksList = MutableLiveData<List<DBLinks>>()

    fun getLinksFromServer(dbUser: DBUser, page: Int, size: Int) {
        viewModelScope.launch {
            try {
                linksResponse.postValue(linksRepository.getLinks(dbUser, page, size))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun getLinksFromDB() {
        linksList = MutableLiveData<List<DBLinks>>()
        viewModelScope.launch {
            try {
                linksList.postValue(linksRepository.getLinksFromDB())
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }


}