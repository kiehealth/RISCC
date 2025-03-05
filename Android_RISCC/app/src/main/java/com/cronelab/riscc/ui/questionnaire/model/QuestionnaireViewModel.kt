package com.cronelab.riscc.ui.questionnaire.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.data.database.table.questions.DBActiveQuestionnaireGroup
import com.cronelab.riscc.ui.questionnaire.model.QuestionnaireRepository.*
import com.cronelab.riscc.ui.questionnaire.pojo.Answer
import kotlinx.coroutines.launch

class QuestionnaireViewModel(private val repository: QuestionnaireRepository) : ViewModel() {

    var activeQuestionnaireResponse = MutableLiveData<ActiveQuestionnaireGroupResponse>()
    var groupQuestionnaireResponse = MutableLiveData<GroupQuestionnaireResponse>()
    var questionsResponse = MutableLiveData<QuestionsResponse?>()
    var answerPostResponse = MutableLiveData<AnswerPostResponse>()
    var questionnaireFinishResponse = MutableLiveData<GroupQuestionnaireFinishResponse>()

    fun getActiveQuestionnaire(dbUser: DBUser) {
        viewModelScope.launch {
            try {
                activeQuestionnaireResponse.postValue(repository.getActiveQuestionnaireGroup(dbUser))
                activeQuestionnaireResponse = MutableLiveData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getQuestionsFromServer(dbUser: DBUser, questionnaire: DBActiveQuestionnaireGroup) {
        viewModelScope.launch {
            try {
                questionsResponse.postValue(repository.getQuestions(dbUser, questionnaire))
                questionsResponse = MutableLiveData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun resetObserver() {
        questionsResponse.value = null
    }

    fun getGroupQuestionnaire(dbUser: DBUser) {
        viewModelScope.launch {
            try {
                groupQuestionnaireResponse.postValue(repository.getGroupQuestionnaire(dbUser))
                groupQuestionnaireResponse= MutableLiveData()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun postAnswerToServer(dbUser: DBUser, answer: Answer) {
        viewModelScope.launch {
            try {
                answerPostResponse.postValue(repository.postAnswer(dbUser, answer))
                answerPostResponse =MutableLiveData()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun finishGroupQuestionnaire(dbUser: DBUser, dbActiveQuestionnaireGroup: DBActiveQuestionnaireGroup) {
        viewModelScope.launch {
            try {
                questionnaireFinishResponse.postValue(repository.finishGroupQuestionnaire(dbUser, dbActiveQuestionnaireGroup))
                questionnaireFinishResponse = MutableLiveData()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}