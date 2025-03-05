package com.cronelab.riscc.ui.notification.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class NotificationVMFactory(private val repository: NotificationRepository):
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotificationViewModel(repository) as T
    }
}