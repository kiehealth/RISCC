package com.cronelab.riscc.ui.aboutUs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class AboutUsViewModelFactory (val repository: AboutUsRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AboutUsViewModel(repository) as T
    }
}