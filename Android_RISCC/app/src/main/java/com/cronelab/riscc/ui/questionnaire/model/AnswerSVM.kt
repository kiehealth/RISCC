package com.cronelab.riscc.ui.questionnaire.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cronelab.riscc.ui.questionnaire.pojo.Answer

/**
 * shared view model
 */
class AnswerSVM : ViewModel() {
    var message = MutableLiveData<String>()
    var answer = MutableLiveData<Answer>()

    fun sendMessage(text: String) {
        this.message.value = text
    }

    fun sendAnswer(answer: Answer?) {
        this.answer.value = answer
    }
}