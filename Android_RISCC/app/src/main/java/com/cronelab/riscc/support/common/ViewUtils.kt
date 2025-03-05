package com.cronelab.riscc.support.common

import android.graphics.drawable.GradientDrawable
import android.view.View

object ViewUtils {
    fun setBgColor(view: View, colorCode: Int) {
        val drawable = view.background as GradientDrawable
        drawable.setColor(colorCode)
    }
}