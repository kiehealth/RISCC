package com.cronelab.riscc.ui.dashboard.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class AppVersionFactory (val repository: AppVersionRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppVersionViewModel(repository) as T
    }
}