package com.cronelab.riscc.support.constants

import com.cronelab.riscc.BuildConfig



val debug = BuildConfig.DEBUG
//const val developerMode = false

//const val BASE_URL_UAT = "http://13.48.128.198:8080"
//const val BASE_URL_PRODUCTION = "http://13.48.149.71:8080"

/*
val BASE_URL: String
    get() = when {
        debug -> BASE_URL_UAT
        else -> BASE_URL_PRODUCTION
        test_diary2021@yopmail.com
    }*/

//val BASE_URL: String
//    get() = when {
////        developerMode -> BASE_URL_UAT
////        else -> BASE_URL_PRODUCTION
//        BuildConfig.SERVER_URL
//    }

const val URL_LOGIN = "/api/auth"
const val URL_SIGN_UP = "/api/public/sign_up"
const val URL_SIGN_UP_VERIFICATION_CODE = "/api/public/sign_up/verification_code"

//const val URL_GET_PASSWORD_RESET_CODE = "public/password_reset/verification_code?identity="
const val URL_GET_PASSWORD_RESET_CODE = "/api/public/password_reset/verification_code"
const val URL_RESET_PASSWORD = "/api/public/reset_password"
const val URL_CHANGE_PASSWORD = "/api/user/change_password"
const val URL_UPDATE_USER = "/api/user"

const val URL_NOTIFICATION = "/api/notification"
const val URL_GET_NOTE = "/api/note"
const val URL_DELETE_NOTE = "/api/note/{noteId}"
const val URL_Link = "/api/link"
const val URL_FEEDBACK = "/api/feedback"
const val URL_GET_ACTIVE_QUESTIONNAIRE = "/api/group_questionnaire/active"
const val URL_UNANSWERED_QUESTIONNAIRE = "/api/question/unanswered_by_questionnaire"
const val URL_QUESTIONNAIRE = "/api/question/by_questionnaire"
const val URL_POST_ANSWER = "/api/answer"
const val URL_GROUP_QUESTIONNAIRE = "/api/group_questionnaire"
const val URL_GROUP_QUESTIONNAIRE_ANSWER_FINISHED = "/api/group_questionnaire_answer_finished"
const val URL_GET_ANSWERED_QUESTIONS = "/api/answer/by_group_questionnaire/{questionnaireId}"
const val URL_GET_APP_VERSION = "/api/app_version"
const val URL_GET_ABOUT_US = "/api/about_us"
const val URL_LOG_OUT = "/api/user/logout"
//const val URL_PRIVACY_POLICY = "https://staff.ki.se/data-protection-policy"
const val URL_PRIVACY_POLICY = "/privacy_policy.html"
const val URL_PRIVACY_POLICY_SE = "https://medarbetare.ki.se/dataskydd-och-datasakerhet"
//const val URL_ABOUT_US = "https://ki.se/en/research/ehealth-core-facility"
const val URL_ABOUT_US = "http://riscc-vaccination.org/saved_file/resource_file/1678864083077.pdf"
const val URL_RESOURCES = "/api/public/resource_file"
const val URL_GET_APP_ANALYTICS_TYPE = "/api/public/app_analytics_key"
const val URL_POST_APP_ANALYTICS = "/api/public/app_analytics"

const val URL_CALCULATE_RISCC = "/api/public/calculate_riscc"








