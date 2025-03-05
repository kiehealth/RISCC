//
//  Constants.swift
//  RISCC
//
//  Created by Jeevan Chaudhary on 18/03/2021.
//

import Foundation
import UIKit


#if DEBUG
    let PUSH_NOTIFICATION_MODE                      = "SANDBOX"
//    let URL_BASE                                    = "http://192.168.101.11:8080"   //UAT
    let URL_BASE                                    = "http://riscc-vaccination.org"   //Production


#else
    let PUSH_NOTIFICATION_MODE                      = "DIST"
    let URL_BASE                                    = "http://riscc-vaccination.org"   //Production

#endif

let DEVICE_IOS                                      = "IOS"
let HEADER_VIEW_HEIGHT                              = CGFloat(88.0)

let QUESTION_TYPE_OTHER                             = "OTHER"
let ANSWER_TYPE_OTHER                               = "OTHER"
let GENDER                                          = [NSLocalizedString("MALE", comment: ""), NSLocalizedString("FEMALE", comment: ""), NSLocalizedString("OTHER", comment: "")]

let NOTIFICATION_IS_USER_ONLINE                     = "Online"

enum QUESTION_TYPE: String {case None = "NONE", DataInput = "DATA_INPUT", SelectOne = "SELECT_ONE", SelectMore = "SELECT_MORE_THAN_ONE", Rating = "RATING"}

enum RESOURCE_FILE_TYPE: String { case Consent = "CONSENT", ConsentSe = "CONSENT_SE", ConsentES = "CONSENT_ES", PrivacyPolicy = "PRIVACY_POLICY", PrivacyPolicySe = "PRIVACY_POLICY_SE", PrivacyPolicyES = "PRIVACY_POLICY_ES", AboutUs = "ABOUT_US", AboutUsSe = "ABOUT_US_SE", AboutUsES = "ABOUT_US_ES" }

enum ERROR_PHONE_NUMBER: Error {
    case invalid
}
enum EMAIL_EXTENSION: String {case Staff = "@ki.se", Student = "@stud.ki.se"}
enum TAB_INDEX: Int {case Survey = 0, Answer = 1, Notification = 2, Note = 3, Link = 4}

enum ANALYTICS_TYPE: String {case UserIn = "USER_IN", UserOut = "USER_OUT", TutorialOut = "TUTORIAL_OUT", TutorialIn = "TUTORIAL_IN", SettingsOut = "SETTINGS_OUT", SettingsIn = "SETTINGS_IN", ProfileOut = "PROFILE_OUT", ProfileIn = "PROFILE_IN", NotificationOut = "NOTIFICATION_OUT", NotificationIn = "NOTIFICATION_IN", NotificationDetailOut = "NOTIFICATION_DETAIL_OUT", NotificationDetailIn = "NOTIFICATION_DETAIL_IN", LinkOut = "LINK_OUT", LinkIn = "LINK_IN", LinkDetailOut = "LINK_DETAIL_OUT", LinkDetailIn = "LINK_DETAIL_IN", NoteOut = "NOTE_OUT", NoteIn = "NOTE_IN", NoteDetailOut = "NOTE_DETAIL_OUT", NoteDetailIn = "NOTE_DETAIL_IN", FeedbackOut = "FEEDBACK_OUT", FeedbackIn = "FEEDBACK_IN", ChangePasswordOut = "CHANGE_PASSWORD_OUT", ChangePasswordIn = "CHANGE_PASSWORD_IN", YourAnswerOut = "YOUR_ANSWER_OUT", YourAnswerIn = "YOUR_ANSWER_IN", MenuOut = "MENU_OUT", MenuIn = "MENU_IN"}

class Constants {
    
    struct Database {
        static let DBMODEL_NAME = "RISCC_SharkORM"
        static let DBMODEL_VERSION = 3
    }
    
    struct Color {
        static let COLOR_BASE = "#FF7B70"
        static let COLOR_COSTUME_BLUE = "#294189"
        static let COLOR_COSTUME_GRAY = "#999999"
        static let COLOR_BORDER = "#E4E4E4"
        static let COLOR_SETTING_IMAGE = "#78909C"
        static let COLOR_COSTUME_DARK_GRAY = "#313131"
        static let COLOR_PLACEHOLDER_TEXT = "#BDBDBD"
        static let BUTTON_LIGHT_GRAY = "E7E7E7"
        static let BUTTON_GRAY = "6E6E6E"
    }
    
    struct Timer {
        static let TOAST_DURATION = 2.0
        static let USER_OFFLINE_NOTIFICATION_TIME = 0
    }
    
    struct Message {
        static let MSG_NETWORK_FAIL = "Please check your network connection and try again."
        static let ENTER_MESSAGE = "Enter Message"
    }
    
    struct Storyboard {
        static let MAIN = "Main"
        static let MAIN_TAB = "MainTab"
        static let UTILITY = "Utility"
        static let SURVEY = "Survey"
        static let NOTIFICATION = "Notification"
        static let LINKS = "Links"
        static let FEEDBACK = "Feedback"
        static let SETTING = "Setting"
        static let SIDE_MENU = "SideMenu"
        static let NOTE = "Note"
        static let ANSWER = "Answer"
    }
    
    struct StoryboardId {
        static let LOGIN_VC = "LoginVC"
        static let REGISTER_VC = "RegisterVC"
        static let FORGOT_PASSWORD_VC = "ForgotPasswordVC"
        static let TUTORIAL_VC = "TutorialVC"
        static let SURVEY_VC = "SurveyVC"
        static let MAIN_TAB_VC = "MainTabVC"
        static let HOME_VC = "HomeVC"
        static let LINKS_VC = "LinksVC"
        static let Links_DETAIL_VC = "LinksDetailVC"
        static let PROFILE_VC = "ProfileVC"
        static let WEB_VIEW_VC = "WebViewVC"
        static let CHANGE_PASSWORD_VC = "ChangePasswordVC"
        static let NOTIFICATION_VC = "NotificationVC"
        static let NOTIFICATION_DETAIL_VC = "NotificationDetailVC"
        static let IMAGE_VIEW_VC = "ImageViewVC"
        static let FEEDBACK_VC = "FeedbackVC"
        static let CONTACT_ADD_EDIT_VC = "ContactAddEditVC"
        static let CONTACT_US_VC = "ContactUsVC"
        static let CONTACT_TAB_VC = "ContactTabVC"
        
        //Note
        static let NOTE_VC = "NoteVC"
        static let NOTE_ADD_EDIT_VC = "NoteAddEditVC"
        static let NOTE_DETAIL_VC = "NoteDetailVC"
        
        //SideMenu
        static let SIDE_MENU_VC = "SideMenuVC"
        
        //Answer
        static let ANSWERED_QUESTION_VC = "AnsweredQuestionVC"
        static let ANSWER_VC = "AnswerVC"
        static let EDIT_ANSWER_VC = "EditAnswerVC"
        
        //Utility
        static let APP_UPDATE_VC = "AppUpdateVC"
    }
    
    struct CellIdentifier {
        static let APP_TUTORIAL = "CellAppTutorial"
        static let INFO = "CellInfo"
        static let MY_CONTACT = "CellMyContact"
        static let NOTE = "CellNote"
        static let NOTIFICATION = "CellNotification"
        
        //CELL QUESTION
        static let QUESTIONNER = "CellQuestioner"
        static let QUESTION_TYPE_DATA_INPUT = "CellQuestionTypeDataInput"
        static let QUESTION_TYPE_RATING = "CellQuestionTypeRating"
        static let QUESTION_TYPE_SELECT_MORE = "CellQuestionTypeSelectMore"
        static let QUESTION_TYPE_SELECT_ONE = "CellQuestionTypeSelectOne"
        
        //CELL ANSWERED QUESTION
        static let ANSWERED_QUESTION = "CellAnsweredQuestion"
        static let ANSWERED_QUESTION_TYPE_DATA_INPUT = "CellAnsweredQuestionTypeDataInput"
        static let ANSWERED_QUESTION_TYPE_RATING = "CellAnsweredQuestionTypeRating"
        static let ANSWERED_QUESTION_TYPE_SELECT_MORE = "CellAnsweredQuestionTypeSelectMore"
        static let ANSWERED_QUESTION_TYPE_SELECT_ONE = "CellAnsweredQuestionTypeSelectOne"
        
        //CELL SIDE MENU
        static let SIDE_MENU = "CellSideMenu"
    }
    
    struct API {
        static let URL_POST_LOGIN = "/api/auth"
        static let URL_GET_REFRESH_TOKEN = "/api/auth/refresh"
        static let URL_POST_SIGN_UP = "/api/public/sign_up"
        static let URL_POST_GET_SIGN_UP_VERIFICATION_CODE = "/api/public/sign_up/verification_code"
        static let URL_PUT_RESET_PASSWORD = "/api/public/reset_password"
        static let URL_GET_PASSWORD_RESET_VERIFICATION_CODE = "/api/public/password_reset/verification_code"
        static let URL_GET_COUNTRY = "/api/public/country"
        static let URL_GET_NOTIFICATION = "/api/notification" //Get Personal notification + Broadcasted notification
        static let URL_GET_NOTIFICATION_BROADCAST = "/api/notification/broadcasted"
        static let URL_POST_NOTIFICATION = "/api/notification/send"
        static let URL_PUT_UPDATE_USER = "/api/user"
        static let URL_PUT_CHANGE_PASSWORD = "/api/user/change_password"
        static let URL_GET_LOGOUT = "/api/user/logout"

        static let URL_GET_QUESTION = "/api/question"
        static let URL_POST_ANSWER = "/api/answer"
        static let URL_GET_QUESTION_UNANSWERED = "/api/questionnaire/active"
        static let URL_GET_QUESTION_ANSWERED = "/api/question/answered"
        static let URL_GET_PAGINATED_LIST_OF_QUESTIONNAIRE = "/api/questionnaire/fields"
        static let URL_GET_QUESTION_BY_QUESTIONNAIRE = "/api/question/by_questionnaire"
        static let URL_GET_QUESTION_UNANSWERED_BY_QUESTIONNAIRE = "/api/question/unanswered_by_questionnaire"
        static let URL_GET_GROUP_QUESTIONNAIRE_ACTIVE = "/api/group_questionnaire/active"
        static let URL_GET_GROUP_QUESTIONNAIRE = "/api/group_questionnaire"
        static let URL_POST_FINISH_GROUP_QUESTIONNAIRE = "/api/group_questionnaire_answer_finished"

        static let URL_GET_ANSWER_BY_GROUP_QUESTIONNAIRE = "/api/answer/by_group_questionnaire/"
        
        static let URL_GET_LINK = "/api/link"
        static let URL_GET_ABOUT_US = "/api/about_us"

        static let URL_GET_NOTES = "/api/note"
        static let URL_ADD_UPDATE_NOTE = "/api/note"
        static let URL_DELETE_NOTE = "/api/note/"

        static let URL_POST_FEEDBACK = "/api/feedback"

        static let URL_GET_APP_ANALYTICS_TYPE = "/api/public/app_analytics_key"
        static let URL_POST_APP_ANALYTICS = "/api/public/app_analytics"

        static let URL_GET_RESOURCE_FILE = "/api/resource_file"
        static let URL_GET_PUBLIC_RESOURCE_FILE = "/api/public/resource_file"
        
        static let URL_GET_APP_VERSION = "/api/app_version"
        
        static let URL_GET_RISCC_VALUE = "/api/public/calculate_riscc"
    }
}



