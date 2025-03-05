package com.cronelab.riscc.ui.answer.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.data.responses.GroupQuestionnaireApiResponse
import kotlinx.coroutines.launch

class AnswerViewModel(private val repository: AnswerRepository) : ViewModel() {

    var answerResponse = MutableLiveData<AnswerRepository.AnswerResponse>()
    var calculatedRisccResponse = MutableLiveData<AnswerRepository.RisccCalculatedRespone>()

    fun getAnswerFromServer(dbUser: DBUser, questionnaire: GroupQuestionnaireApiResponse.GroupQuestionnaire) {
        viewModelScope.launch {
            answerResponse.postValue(repository.getAnsweredQuestion(dbUser,questionnaire))
        }
    }
    fun getCalculatedRisccForGivenQuestionnaireFromServer(dbUser: DBUser, questionnaireId: String) {
        viewModelScope.launch {
            calculatedRisccResponse.postValue(repository.getCalculatedRiscForGivenQuestionnaire(dbUser,questionnaireId))
        }
    }
}