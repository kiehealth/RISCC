package com.cronelab.riscc.ui.feedback.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class FeedbackViewModelFactory(private val repository: FeedbackRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FeedbackViewModel(repository) as T
    }
}