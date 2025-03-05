package com.cronelab.riscc.support.common

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.cronelab.riscc.R
import com.google.android.material.snackbar.Snackbar

fun Context.showToast(message: String) {
    val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = inflater.inflate(R.layout.custom_toast, null)
    val textView: TextView = view.findViewById(R.id.toastMessageTV)
    //setBackgroundColor(view.findViewById(R.id.custom_toast_layout))
    textView.text = message
    val toast = Toast(this)
    toast.setGravity(Gravity.BOTTOM, 0, 350)
    toast.duration = Toast.LENGTH_SHORT
    toast.view = view
    toast.show()
}

private fun setBackgroundColor(customToastLayout: View) {
    val primaryColor = Color.parseColor("#00ACF1")
    val gradientDrawable: GradientDrawable = customToastLayout.background as GradientDrawable
    gradientDrawable.setColor(primaryColor)
}