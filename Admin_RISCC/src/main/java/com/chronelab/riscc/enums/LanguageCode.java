package com.chronelab.riscc.enums;

public enum LanguageCode {
    ENGLISH,
    SWEDISH,
    SPANISH;

    public static boolean exists(String str) {
        for (LanguageCode languageCode : LanguageCode.values()) {
            if (languageCode.name().equals(str)) {
                return true;
            }
        }
        return false;
    }
}
