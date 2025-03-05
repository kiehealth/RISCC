package com.cronelab.riscc.ui.resources

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


@Suppress("UNCHECKED_CAST")
class ResourceViewModelFactory(val repository: ResourceRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ResourceViewModel(repository) as T
    }
}