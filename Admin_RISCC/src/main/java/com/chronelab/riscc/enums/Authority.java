package com.chronelab.riscc.enums;

public enum Authority {
    COUNTRY_C("Country (Create)"), COUNTRY_U("Country (Update)"), COUNTRY_D("Country (Delete)"),
    STATE_C("State (Create)"), STATE_U("State (Update)"), STATE_D("State (Delete)"),
    CITY_C("City (Create)"), CITY_U("City (Update)"), CITY_D("City (Delete)"),
    AUTHORITY("Authority"), AUTHORITY_C("Authority (Create)"), AUTHORITY_U("Authority (Update)"), AUTHORITY_D("Authority (Delete)"),
    ROLE("Role"), ROLE_RA("Role (Read All)"), ROLE_C("Role (Create)"), ROLE_U("Role (Update)"), ROLE_D("Role (Delete)"),
    USER("User"), USER_RA("User (Read All)"), USER_C("User (Create)"), USER_U("User (Update)"), USER_D("User (Delete)"), CHANGE_PASSWORD("Change Password"),

    QUESTION_TYPE("Question Type"), QUESTION_TYPE_C("Question Type (Create)"), QUESTION_TYPE_U("Question Type (Update)"), QUESTION_TYPE_D("Question Type (Delete)"),
    QUESTIONNAIRE("Questionnaire"), QUESTIONNAIRE_C("Questionnaire (Create)"), QUESTIONNAIRE_U("Questionnaire (Update)"), QUESTIONNAIRE_D("Questionnaire (Delete)"),
    QUESTION("Question"), QUESTION_C("Question (Create)"), QUESTION_U("Question (Update)"), QUESTION_D("Question (Delete)"),
    LINK("Link"), LINK_C("Link (Create)"), LINK_U("Link (Update)"), LINK_D("Link (Delete)"),
    APP_VERSION_U("App Version (Update)"),
    NOTIFICATION("Notification"), NOTIFICATION_C("Notification (Create)"), NOTIFICATION_RA("Notification (Read All)"), NOTIFICATION_D("Notification (Delete)"),
    FEEDBACK("Feedback"), FEEDBACK_C("Feedback (Create)"), FEEDBACK_RA("Feedback (Read All)"), FEEDBACK_D("Feedback (Delete)"),
    ANSWER_RA("Answer (Read All)"), ANSWER_D("Answer (Delete)"),
    SETTING("Setting"), SETTING_U("Setting (Update)"),
    APP_ANALYTICS("App Analytics"),

    NOTE("Note"), NOTE_C("Note (Create)"), NOTE_U("Note (Update)"), NOTE_D("Note (Delete)"),

    GROUP("Group"), GROUP_RA("Group (Read All)"), GROUP_C("Group (Create)"), GROUP_U("Group (Update)"), GROUP_D("Group (Delete)"),

    ALLOWED_REGISTRATION("Allowed Registration"), ALLOWED_REGISTRATION_C("Allowed Registration (Create)"), ALLOWED_REGISTRATION_U("Allowed Registration (Update)"), ALLOWED_REGISTRATION_D("Allowed Registration (Delete)"),

    MULTI_LANG_MESSAGE("Multi Lang Message"), MULTI_LANG_MESSAGE_C("Multi Lang Message (Create)"), MULTI_LANG_MESSAGE_U("Multi Lang Message (Update)"), MULTI_LANG_MESSAGE_D("Multi Lang Message (Delete)");

    private final String displayTitle;

    Authority(String displayTitle) {
        this.displayTitle = displayTitle;
    }

    public String getDisplayTitle() {
        return this.displayTitle;
    }
}
