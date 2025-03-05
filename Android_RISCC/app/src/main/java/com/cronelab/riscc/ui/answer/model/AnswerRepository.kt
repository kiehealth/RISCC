package com.cronelab.riscc.ui.answer.model

import com.cronelab.riscc.R
import com.cronelab.riscc.support.AppController
import com.cronelab.riscc.support.data.ApiFactory
import com.cronelab.riscc.support.data.SafeApiRequest
import com.cronelab.riscc.support.data.database.configuration.AppDatabase
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.data.database.table.answer.Answer
import com.cronelab.riscc.support.data.database.table.questions.DBActiveQuestionnaireGroup
import com.cronelab.riscc.support.data.responses.CalculatedRISCCApiResponse
import com.cronelab.riscc.support.data.responses.GroupQuestionnaireApiResponse
import com.cronelab.riscc.support.manager.LanguageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnswerRepository(
    private val apiFactory: ApiFactory,
    private val appDatabase: AppDatabase
) : SafeApiRequest() {

    class AnswerResponse {
        var error: Throwable? = null
        var message = ""
        var answerList: List<Answer> = ArrayList()
    }
    class RisccCalculatedRespone {
        var error: Throwable? = null
        var message = ""
        var moreInfo: String = ""
    }

    private var answerResponse = AnswerResponse()
    private var calculatedRISCCApiResponse = RisccCalculatedRespone()

    suspend fun getAnsweredQuestion(user: DBUser, questionnaire: GroupQuestionnaireApiResponse.GroupQuestionnaire): AnswerResponse {
        return with(Dispatchers.IO) {
            getAnsweredQuestionFromServer(user, questionnaire)
            answerResponse
        }
    }

    private suspend fun getAnsweredQuestionFromServer(user: DBUser, questionnaire: GroupQuestionnaireApiResponse.GroupQuestionnaire) {
        val answerResponse = AnswerResponse()
        try {
            val apiResponse = apiRequest { apiFactory.getAnsweredQuestion(user.token,
                LanguageManager.getLanguageCode(),
                questionnaire.id.toString()) }
            answerResponse.message = apiResponse.message
            if (apiResponse.status) {
                answerResponse.answerList = apiResponse.data!!.answerList!!
                answerResponse.error = null
            } else {
                answerResponse.error = Throwable()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            answerResponse.error = ex
            answerResponse.message = AppController.instance.applicationContext.getString(R.string.unableToGetData)
        }
        this.answerResponse = answerResponse
    }

    suspend fun getCalculatedRiscForGivenQuestionnaire(dbUser: DBUser, questionnaireId: String): RisccCalculatedRespone {
        return withContext(Dispatchers.IO) {
            calculateRisccForGivenQuestionnaire(dbUser, questionnaireId)
            calculatedRISCCApiResponse
        }
    }

    private suspend fun calculateRisccForGivenQuestionnaire(dbUser: DBUser, questionnaireId: String) {
        val calculatedRISCCApiResponse = RisccCalculatedRespone()
        try {
            val apiResponse = apiRequest {
                apiFactory.getCalculatedRISCCForGivenQuestionnaire(
                    dbUser.token,
                    LanguageManager.getLanguageCode(),
                    questionnaireId,
                    dbUser.id,
                )
            }
            calculatedRISCCApiResponse.message = apiResponse.message!!
            if (apiResponse.status) {
                for(risccValue in apiResponse.data!!.risccValue!!){
                    calculatedRISCCApiResponse.message = risccValue.message!!
                    calculatedRISCCApiResponse.moreInfo = risccValue.moreInfo ?: ""
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            calculatedRISCCApiResponse.message = AppController.instance.applicationContext.getString(R.string.unableToGetData)
        }
        this.calculatedRISCCApiResponse = calculatedRISCCApiResponse
    }
}