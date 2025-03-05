package com.cronelab.riscc.ui.answer.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class AnswerViewModelFactory (private val repository: AnswerRepository): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AnswerViewModel(repository) as T
    }
}