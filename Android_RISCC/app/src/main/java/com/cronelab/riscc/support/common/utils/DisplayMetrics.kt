package com.cronelab.riscc.support.common.utils

import android.app.Activity
import android.util.DisplayMetrics

class DisplayMetrics {
   public fun getHeight(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.getDefaultDisplay().getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

   public fun getWidth(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.getDefaultDisplay().getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }
}