package com.cronelab.riscc.ui.feedback.model

import android.os.Build
import com.cronelab.riscc.support.common.utils.Utils
import com.cronelab.riscc.support.data.ApiFactory
import com.cronelab.riscc.support.data.SafeApiRequest
import com.cronelab.riscc.support.data.database.configuration.AppDatabase
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.manager.LanguageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class FeedbackRepository(
    private val apiFactory: ApiFactory,
    private val appDatabase: AppDatabase
) : SafeApiRequest() {
    class FeedBackResponse {
        var error: Throwable? = null
        var message = ""
    }

    private var feedbackResponse = FeedBackResponse()

    suspend fun submitFeedback(dbUser: DBUser, title: String, feedback: String): FeedBackResponse {
        return withContext(Dispatchers.IO) {
            postFeedback(dbUser, title, feedback)
            feedbackResponse
        }
    }

    private suspend fun postFeedback(dbUser: DBUser, title: String, feedback: String) {
        val feedbackResponse = FeedBackResponse()
        val params: HashMap<String, String> = HashMap()
        params["title"] = title
        params["description"] = feedback
        params["runningOS"] = "Android"
        params["phoneModel"] = Utils.deviceName()

        try {
            val apiResponse = apiRequest { apiFactory.postFeedback(dbUser.token, LanguageManager.getLanguageCode(), params) }
            if (apiResponse.status) {
                feedbackResponse.error = null
            } else {
                feedbackResponse.error = Throwable()
            }
            feedbackResponse.message = apiResponse.message
            this.feedbackResponse = feedbackResponse
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}