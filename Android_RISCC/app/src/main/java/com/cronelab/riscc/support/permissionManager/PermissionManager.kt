package com.cronelab.riscc.support.permissionManager

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class PermissionManager(private val context: Context) {
    private val sessionManager: SessionManager
    fun shouldAskPermission(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    private fun shouldAskPermission(context: Context, permission: String): Boolean {
        if (shouldAskPermission()) {
            val permissionResult = ActivityCompat.checkSelfPermission(context, permission)
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return true
            }
        }
        return false
    }

    fun checkPermission(context: Context, permission: String, listener: PermissionAskListener) {
        if (shouldAskPermission(context, permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((context as AppCompatActivity), permission)) {
                listener.onPermissionPreviouslyDenied()
            } else {
                if (sessionManager.isFirstTimeAskingPermission(permission)) {
                    sessionManager.firstTimeAskingPermission(permission, false)
                    listener.onNeedPermission()
                } else {
                    listener.onPermissionPreviouslyDeniedWithNeverAskAgain()
                }
            }
        } else {
            listener.onPermissionGranted()
        }
    }

    interface PermissionAskListener {
        fun onNeedPermission()
        fun onPermissionPreviouslyDenied()
        fun onPermissionPreviouslyDeniedWithNeverAskAgain()
        fun onPermissionGranted()
    }

    init {
        sessionManager = SessionManager(context)
    }
}