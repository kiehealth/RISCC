package com.cronelab.riscc.ui.questionnaire.model

import android.util.Log
import com.cronelab.riscc.R
import com.cronelab.riscc.support.AppController
import com.cronelab.riscc.support.data.ApiFactory
import com.cronelab.riscc.support.data.SafeApiRequest
import com.cronelab.riscc.support.data.database.configuration.AppDatabase
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.data.database.table.questions.*
import com.cronelab.riscc.support.data.responses.CalculatedRISCCApiResponse
import com.cronelab.riscc.support.data.responses.GroupQuestionnaireApiResponse.GroupQuestionnaire
import com.cronelab.riscc.support.manager.LanguageManager
import com.cronelab.riscc.ui.questionnaire.pojo.Answer
import com.cronelab.riscc.ui.questionnaire.pojo.WelcomeQuestionnaire
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuestionnaireRepository(
    private val apiFactory: ApiFactory,
    private val appDatabase: AppDatabase
) : SafeApiRequest() {

    private val TAG = "QuestionnaireRepository"

    class ActiveQuestionnaireGroupResponse {
        var message = ""
        var error: Throwable? = null
        var activeQuestionnaireGroupList: List<DBActiveQuestionnaireGroup> = ArrayList()
    }

    class QuestionsResponse {
        var message = ""
        var error: Throwable? = null
        var welcomeMessageInfo: WelcomeQuestionnaire? = null
        var questionList: List<DBQuestions> = ArrayList()
    }

    class GroupQuestionnaireResponse {
        var message = ""
        var error: Throwable? = null
        var groupQuestionnaireList: List<GroupQuestionnaire> = ArrayList()
    }

    class AnswerPostResponse {
        var message = ""
        var error: Throwable? = null
    }

    class GroupQuestionnaireFinishResponse {
        var message = ""
        var error: Throwable? = null
    }



    private var activeQuestionnaireGroupResponse = ActiveQuestionnaireGroupResponse()
    private var groupQuestionnaireResponse = GroupQuestionnaireResponse()
    private var questionResponse = QuestionsResponse()

    private var answerPostResponse = AnswerPostResponse()
    private var finishGroupQuestionnaireResponse = GroupQuestionnaireFinishResponse()

    suspend fun getActiveQuestionnaireGroup(dbUser: DBUser): ActiveQuestionnaireGroupResponse {
        return withContext(Dispatchers.IO) {
            requestActiveQuestionnaire(dbUser)
            activeQuestionnaireGroupResponse
        }
    }

    private suspend fun requestActiveQuestionnaire(dbUser: DBUser) {
        val activeQuestionnaireResponse = ActiveQuestionnaireGroupResponse()
        try {
            val apiResponse = apiRequest { apiFactory.getActiveQuestionnaire(dbUser.token, LanguageManager.getLanguageCode()) }
            activeQuestionnaireResponse.message = apiResponse.message
            if (apiResponse.status) {
                activeQuestionnaireResponse.activeQuestionnaireGroupList = apiResponse.data!!.activeQuestionnaireGroups!!
                activeQuestionnaireResponse.error = null
            } else {
                activeQuestionnaireResponse.error = Throwable()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            activeQuestionnaireResponse.error = Throwable()
            activeQuestionnaireResponse.message = AppController.instance.applicationContext.getString(R.string.unableToGetData)
        }
        this.activeQuestionnaireGroupResponse = activeQuestionnaireResponse
    }

    suspend fun getGroupQuestionnaire(dbUser: DBUser): GroupQuestionnaireResponse {
        return with(Dispatchers.IO) {
            getGroupQuestionnaireFromServer(dbUser)
            groupQuestionnaireResponse
        }
    }

    private suspend fun getGroupQuestionnaireFromServer(dbUser: DBUser) {
        val groupQuestionnaireResponse = GroupQuestionnaireResponse()
        try {
            val apiResponse = apiRequest { apiFactory.getGroupQuestionnaire(dbUser.token, LanguageManager.getLanguageCode()) }
            groupQuestionnaireResponse.message = apiResponse.message
            if (apiResponse.status) {
                groupQuestionnaireResponse.error = null
                groupQuestionnaireResponse.groupQuestionnaireList = apiResponse.data?.groupQuestionnaireList!!
            } else {
                groupQuestionnaireResponse.error = Throwable()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            groupQuestionnaireResponse.error = ex
            groupQuestionnaireResponse.message = AppController.instance.applicationContext.getString(R.string.unableToGetData)
        }
        this.groupQuestionnaireResponse = groupQuestionnaireResponse
    }


    suspend fun getQuestions(dbUser: DBUser, dbActiveQuestionnaireGroup: DBActiveQuestionnaireGroup): QuestionsResponse {
        return withContext(Dispatchers.IO) {
            requestQuestions(dbUser, dbActiveQuestionnaireGroup)
            questionResponse
        }
    }


    private suspend fun requestQuestions(dbUser: DBUser, dbActiveQuestionnaireGroup: DBActiveQuestionnaireGroup) {
        val questionResponse = QuestionsResponse()
        try {
            val apiResponse = apiRequest {
                apiFactory.getUnAnsweredQuestionnaire(
                    dbUser.token,
                    LanguageManager.getLanguageCode(),
                    dbActiveQuestionnaireGroup.questionnaire!!.questionnaireId.toString(),
                    dbActiveQuestionnaireGroup.id.toString()
                )
            }
            Log.w(TAG, " 1 questionsResponse obj very imp : ${Gson().toJson(apiResponse.status)}")
            Log.w(TAG, " 1 questionsResponse obj very imp : ${Gson().toJson(apiResponse.data)}")
            Log.w(TAG, " 1 questionsResponse obj very imp : ${Gson().toJson(apiResponse.data?.welcome)}")
            Log.w(TAG, " 1 questionsResponse obj very imp : ${Gson().toJson(apiResponse.message)}")

            questionResponse.message = apiResponse.message
            if (apiResponse.status) {
                val questionList = ArrayList<DBQuestions>()
                for (question in apiResponse.data!!.questionsList!!) {
                    question.groupQuestionnaireId = dbActiveQuestionnaireGroup.id
                    questionList.add(question)
                }
//                questionResponse.questionList = apiResponse.data!!.questionsList!!
                questionResponse.questionList = questionList
                questionResponse.welcomeMessageInfo = apiResponse.data?.welcome
                questionResponse.error = null
            } else {
                questionResponse.error = Throwable()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            questionResponse.message = AppController.instance.applicationContext.getString(R.string.unableToGetData)
        }
        Log.w(TAG, "2 questionsResponse obj very imp : ${Gson().toJson(questionResponse)}")
        this.questionResponse = questionResponse
    }

    suspend fun postAnswer(dbUser: DBUser, answer: Answer): AnswerPostResponse {
        return withContext(Dispatchers.IO) {
            postAnswerToServer(dbUser, answer)
            answerPostResponse
        }
    }

    private suspend fun postAnswerToServer(user: DBUser, answer: Answer) {
        val answerPostResponse = AnswerPostResponse()

        try {
            Log.w(TAG, "answerObj ${Gson().toJson(answer)}")
            val apiResponse = apiRequest { apiFactory.postAnswer(user.token, LanguageManager.getLanguageCode(), answer) }
            answerPostResponse.message = apiResponse.message
        } catch (ex: Exception) {
            ex.printStackTrace()
            answerPostResponse.message = AppController.instance.applicationContext.getString(R.string.unableToGetData)
            answerPostResponse.error = ex
        }
        this.answerPostResponse = answerPostResponse
    }

    suspend fun finishGroupQuestionnaire(user: DBUser, dbActiveQuestionnaireGroup: DBActiveQuestionnaireGroup): GroupQuestionnaireFinishResponse {
        return withContext(Dispatchers.IO) {
            requestFinishGroupQuestionnaire(user, dbActiveQuestionnaireGroup)
            finishGroupQuestionnaireResponse
        }
    }

    private suspend fun requestFinishGroupQuestionnaire(user: DBUser, dbActiveQuestionnaireGroup: DBActiveQuestionnaireGroup) {
        val finishGroupQuestionnaireResponse = GroupQuestionnaireFinishResponse()
        try {
            val body = HashMap<String, String>()
            body["groupQuestionnaireId"] = dbActiveQuestionnaireGroup.id.toString()
            val apiResponse = apiRequest { apiFactory.finishQuestionnaire(user.token, LanguageManager.getLanguageCode(), body) }
            finishGroupQuestionnaireResponse.message = apiResponse.message + ""
            if (apiResponse.status) {
                finishGroupQuestionnaireResponse.error = null
            } else {
                finishGroupQuestionnaireResponse.error = Throwable()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            finishGroupQuestionnaireResponse.error = ex
            finishGroupQuestionnaireResponse.message = AppController.instance.getString(R.string.unableToFinishQuestionnaire)
        }
        this.finishGroupQuestionnaireResponse = finishGroupQuestionnaireResponse
    }
}