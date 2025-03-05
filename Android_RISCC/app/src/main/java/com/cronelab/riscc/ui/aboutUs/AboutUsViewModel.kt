package com.cronelab.riscc.ui.aboutUs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cronelab.riscc.support.data.database.table.DBUser
import kotlinx.coroutines.launch

class AboutUsViewModel(var repository: AboutUsRepository) : ViewModel() {
    var aboutUs = MutableLiveData<AboutUsRepository.AboutUsResponse>()

    fun getAboutUsData(dbUser: DBUser) {
        viewModelScope.launch {
            try {
                aboutUs.postValue(repository.getAboutUsData(dbUser))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

}