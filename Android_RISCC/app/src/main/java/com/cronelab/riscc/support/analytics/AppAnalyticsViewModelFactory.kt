package com.cronelab.riscc.support.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


@Suppress("UNCHECKED_CAST")
class AppAnalyticsViewModelFactory(val repository: AppAnalyticsRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppAnalyticsViewModel(repository) as T
    }
}