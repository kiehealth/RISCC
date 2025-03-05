//const BASE_URL = "http://localhost:8080/api/";
export const BASE_URL = window.location.protocol + "//" + window.location.host;

export const AUTH = BASE_URL + "/api/auth";
export const PASSWORD_VERIFICATION_CODE = BASE_URL + "/api/public/password_reset/verification_code?identity=";
export const RESET_PASSWORD = BASE_URL + "/api/public/reset_password";

export const ROLE = BASE_URL + "/api/role";

export const AUTHORITY = BASE_URL + "/api/authority";

export const USER = BASE_URL + "/api/user";
export const USER_BY_ROLE = BASE_URL + "/api/user/by_role/";
export const USER_CHANGE_PASSWORD = BASE_URL + "/api/user/change_password";
export const USER_FIELDS = "/api/user/fields?";

export const QUESTION = BASE_URL + "/api/question";
export const QUESTION_BY_QUESTIONNAIRE = BASE_URL + "/api/question/by_questionnaire?questionnaireId=";
export const QUESTION_IMPORT = BASE_URL + "/api/question/data_import";
export const QUESTION_TEMPLATE = BASE_URL + "/api/question/download_template";

export const QUESTIONNAIRE_FIELDS = BASE_URL + "/api/questionnaire/fields";
export const QUESTION_TYPE = BASE_URL + "/api/question_type";
export const QUESTIONNAIRE = BASE_URL + "/api/questionnaire";

export const FEEDBACK = BASE_URL + "/api/feedback";

export const APP_VERSION = BASE_URL + "/api/app_version";
export const SETTING = BASE_URL + "/api/setting";
export const WELCOME = BASE_URL + "/api/setting/welcome";
export const ABOUT_US = BASE_URL + "/api/about_us";
export const RESOURCE_FILE = BASE_URL + "/api/resource_file";
export const RESOURCE_FILE_PUBLIC = BASE_URL + "/api/public/resource_file";

export const LINK = BASE_URL + "/api/link";

export const NOTIFICATION = BASE_URL + "/api/notification";

export const APP_ANALYTICS_KEY = BASE_URL + "/api/app_analytics_key";
export const APP_ANALYTICS_KEY_PUBLIC = BASE_URL + "/api/public/app_analytics_key";
export const APP_ANALYTICS_KEY_PUBLIC_FILTER = BASE_URL + "/api/public/app_analytics_key/filter?";
export const APP_ANALYTICS_KEY_PAIR = BASE_URL + "/api/app_analytics_key_pair";

export const APP_ANALYTICS_REPORT = BASE_URL + "/api/app_analytics";
export const APP_ANALYTICS_REPORT_FILTER = BASE_URL + "/api/app_analytics/filter";
export const APP_ANALYTICS_REPORT_EXPORT_CSV = BASE_URL + "/api/app_analytics/export/csv?";

export const ANSWER = BASE_URL + "/api/answer";
export const ANSWER_EXPORT_DATE = BASE_URL + "/api/answer/export";
export const ANSWER_BY_USER = BASE_URL + "/api/answer/by_user";

export const GROUP = BASE_URL + "/api/group";

export const ALLOWED_REGISTRATION = BASE_URL + "/api/allowed_registration";
export const ALLOWED_REGISTRATION_IMPORT = BASE_URL + "/api/allowed_registration/data_import";
export const ALLOWED_REGISTRATION_TEMPLATE = BASE_URL + "/api/allowed_registration/download_template";

export const ERROR_MESSAGE = BASE_URL + "/api/multi_lang_message";

export const RISCC_RANGE_VALUE = BASE_URL + "/api/setting/riscc_range";

export const RISCC_CALCULATION = BASE_URL+"/api/public/calculate_riscc";