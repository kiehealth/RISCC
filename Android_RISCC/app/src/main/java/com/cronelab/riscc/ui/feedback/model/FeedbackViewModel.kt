package com.cronelab.riscc.ui.feedback.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cronelab.riscc.support.data.database.table.DBUser
import kotlinx.coroutines.launch

class FeedbackViewModel(private val feedbackRepository: FeedbackRepository) : ViewModel() {

    val TAG = "FeedbackViewModel"
    val feedbackResponse = MutableLiveData<FeedbackRepository.FeedBackResponse>()

    fun submitFeedback(dbUser: DBUser, title: String, description: String) {
        viewModelScope.launch {
            try {
                feedbackResponse.postValue(feedbackRepository.submitFeedback(dbUser, title, description))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

}