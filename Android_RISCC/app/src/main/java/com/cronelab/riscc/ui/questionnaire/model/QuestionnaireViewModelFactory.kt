package com.cronelab.riscc.ui.questionnaire.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class QuestionnaireViewModelFactory(private val repository: QuestionnaireRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QuestionnaireViewModel(repository) as T
    }
}