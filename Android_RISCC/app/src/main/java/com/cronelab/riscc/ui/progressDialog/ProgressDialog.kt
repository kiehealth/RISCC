package com.cronelab.riscc.ui.progressDialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.cronelab.riscc.R

class ProgressDialog {
    companion object {
        fun loadingProgressDialog(context: Context, message: String): Dialog {
            val dialog = Dialog(context)
            val view = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null)
            val textView =view.findViewById<TextView>(R.id.textView)
            textView.text = message

            dialog.setContentView(view)
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val metrics = context.getResources().getDisplayMetrics()
            val width = metrics.widthPixels

            dialog.getWindow()!!.setLayout(
                6 * width / 7,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            return dialog
        }
    }
}