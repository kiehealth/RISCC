package com.cronelab.riscc.support.common.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Build.VERSION_CODES
import android.view.View
import android.view.inputmethod.InputMethodManager

object Utils {
    fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun isKeyboardShown(context: Context?, view: View?): Boolean {
        if (context == null || view == null) {
            return false
        }
        val imm = context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun isValidPhoneNumber(phonenumber: String): Boolean {
        return android.util.Patterns.PHONE.matcher(phonenumber).matches();
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    fun deviceName(): String {
        val deviceName = (Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + VERSION_CODES::class.java.fields[Build.VERSION.SDK_INT].name)
        return deviceName
    }
}