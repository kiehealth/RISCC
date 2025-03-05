package com.cronelab.riscc.ui.links.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class LinksViewModelFactory(private val repository: LinksRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LinksViewModel(repository) as T
    }
}