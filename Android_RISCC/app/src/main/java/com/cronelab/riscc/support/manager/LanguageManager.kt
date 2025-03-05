package com.cronelab.riscc.support.manager

import java.util.*

object LanguageManager {

    fun getLanguageCode(): String {
        return Locale.getDefault().language.toUpperCase(Locale.ENGLISH)
    }

}