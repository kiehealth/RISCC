package com.cronelab.riscc.support.data

import android.util.Log
import com.cronelab.riscc.BuildConfig
import com.cronelab.riscc.support.constants.*
import com.cronelab.riscc.support.data.responses.*
import com.cronelab.riscc.ui.auth.pojo.UserSignUp
import com.cronelab.riscc.ui.questionnaire.pojo.Answer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiFactory {
    companion object {
        const val TAG = "ApiFactory"

        operator fun invoke(): ApiFactory {
            val httpLoggingInterceptor =
                HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Log.d(TAG, message)
                    }
                }).apply {
                    level = if (debug) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
                }


            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                //.addInterceptor(cacheInterceptor)
                //.addInterceptor(connectionInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .cache(null)
                .build()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiFactory::class.java)
        }
    }

    @Headers("Content-Type:application/json")
    @POST(URL_SIGN_UP)
    suspend fun signUp(
        @Body params: UserSignUp,
        @Header(Constants.LANGUAGE_CODE) code: String
    ): Response<SignUpApiResponse>

    @Headers("Content-Type:application/json")
    @POST(URL_LOGIN)
    suspend fun login(
        @Body params: HashMap<String, String>,
        @Header(Constants.LANGUAGE_CODE) code: String
    ): Response<LoginApiResponse>

    @Headers("Content-Type:application/json")
    @POST(URL_SIGN_UP_VERIFICATION_CODE)
    suspend fun getVerification(
        @Body params: HashMap<String, String>,
        @Header(Constants.LANGUAGE_CODE) code: String
    ): Response<VerificationApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_GET_PASSWORD_RESET_CODE)
    suspend fun getPasswordResetCode(
        @Query("identity") email: String,
        @Header(Constants.LANGUAGE_CODE) code: String
    ): Response<PasswordResetCodeApiResponse>

    @Headers("Content-Type:application/json")
    @PUT(URL_RESET_PASSWORD)
    suspend fun resetPassword(
        @Body params: HashMap<String, String>,
        @Header(Constants.LANGUAGE_CODE) code: String
    ): Response<PasswordResetApiResponse>

    @Headers("Content-Type:application/json")
    @PUT(URL_CHANGE_PASSWORD)
    suspend fun changePassword(
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) code: String,
        @Body params: HashMap<String, String>
    ): Response<ChangePasswordApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_Link)
    suspend fun getLinks(
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) code: String,
        @Query("pageNumber") page: Int,
        @Query("pageSize") size: Int
    ): Response<LinksApiResponse>


    @Headers("Content-Type:application/json")
    @GET(URL_NOTIFICATION)
    suspend fun getNotification(
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) code: String
    ): Response<NotificationApiResponse>

    @Headers("Content-Type:application/json")
    @POST(URL_FEEDBACK)
    suspend fun postFeedback(
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) languageCode: String,
        @Body params: HashMap<String, String>
    ): Response<FeedbackApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_GET_NOTE)
    suspend fun getNote(
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) languageCode: String,
        @Query("pageNumber") page: Int,
        @Query("pageSize") size: Int
    ): Response<NoteApiResponse>

    @Headers("Content-Type:application/json")
    @POST(URL_GET_NOTE) //end point same, only method differs
    suspend fun postNote(
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) languageCode: String,
        @Body params: HashMap<String, String>
    ): Response<NotePostApiResponse>

    @Headers("Content-Type:application/json")
    @PUT(URL_GET_NOTE)
    suspend fun updateNote( //endpoint same, only method different
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) code: String,
        @Body params: HashMap<String, String>
    ): Response<NoteUpdateApiResponse>


    @Headers("Content-Type:application/json")
    @DELETE(URL_DELETE_NOTE)
    suspend fun deleteNote(
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) languageCode: String,
        @Path("noteId") noteId: String
    ): Response<NoteDeleteApiResponse>

    @Headers("Content-Type:application/json")
    @PUT(URL_UPDATE_USER)
    suspend fun updateProfile(
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) code: String,
        @Body bodyParams: HashMap<String, String>
    ): Response<UserUpdateApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_GET_ACTIVE_QUESTIONNAIRE)
    suspend fun getActiveQuestionnaire(
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) code: String
    ): Response<ActiveQuestionnaireApiResponse>

    //http://13.48.128.198:8080/api/question/by_questionnaire?pageNumber=0&pageSize=50&questionnaireId=3
    @Headers("Content-Type:application/json")
    @GET(URL_UNANSWERED_QUESTIONNAIRE)
    suspend fun getUnAnsweredQuestionnaire(
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) code: String,
        @Query("questionnaireId") questionnaireId: String,
        @Query("groupQuestionnaireId") groupQuestionnaireId: String
    ): Response<QuestionsApiResponse>

    //http://riscc-vaccination.org/api/public/calculate_riscc?questionnaireIds=1&userIds=2
    @Headers("Content-Type:application/json")
    @GET(URL_CALCULATE_RISCC)
    suspend fun getCalculatedRISCCForGivenQuestionnaire(
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) code: String,
        @Query("questionnaireIds") questionnaireIds: String,
        @Query("userIds") userIds: String
    ): Response<CalculatedRISCCApiResponse>


    @Headers("Content-Type:application/json")
    @POST(URL_POST_ANSWER)
    suspend fun postAnswer(
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) code: String,
        @Body params: Answer
    ): Response<AnswerPostApiResponse>


    @Headers("Content-Type:application/json")
    @GET(URL_GROUP_QUESTIONNAIRE)
    suspend fun getGroupQuestionnaire(
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) code: String
    ): Response<GroupQuestionnaireApiResponse>


    @Headers("Content-Type:application/json")
    @GET(URL_GET_ANSWERED_QUESTIONS)
    suspend fun getAnsweredQuestion(
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) code: String,
        @Path("questionnaireId") questionnaireId: String

    ): Response<AnswerApiResponse>

    @Headers("Content-Type: application/json")
    @POST(URL_GROUP_QUESTIONNAIRE_ANSWER_FINISHED)
    suspend fun finishQuestionnaire(
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) code: String,
        @Body params: HashMap<String, String>
    ): Response<FinishQuestionnaireApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_GET_APP_VERSION)
    suspend fun getAppVersion(
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) code: String
    ): Response<AppVersionApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_GET_ABOUT_US)
    suspend fun getAboutData(
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) code: String
    ): Response<AboutUsApiResponse>

    @Headers("Content-Type:application/json")
    @PUT(URL_LOG_OUT)
    suspend fun logOut(
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) code: String
    ): Response<LogOutApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_RESOURCES)
    suspend fun getResources(@Header(Constants.LANGUAGE_CODE) code: String): Response<ResourceApiResponse>

    @Headers("Content-Type:application/json")
    @GET(URL_GET_APP_ANALYTICS_TYPE)
    suspend fun getAppAnalyticsType(
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) code: String,
    ): Response<AppAnalyticsTypeApiResponse>

    @Headers("Content-Type:application/json")
    @POST(URL_POST_APP_ANALYTICS)
    suspend fun postAnalytics(
        @Header("Authorization") authorization: String,
        @Header(Constants.LANGUAGE_CODE) code: String,
        @Body params: HashMap<String, String>
    ): Response<AnalyticsPostApiResponse>

}
