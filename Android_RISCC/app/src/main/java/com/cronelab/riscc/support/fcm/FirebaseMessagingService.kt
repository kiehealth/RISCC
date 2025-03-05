package com.cronelab.riscc.support.fcm

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.cronelab.riscc.support.AppController
import com.cronelab.riscc.support.constants.UPDATE_PROFILE_BROADCAST
import com.cronelab.riscc.support.helper.DeviceTokenManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


open class FirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "FirebaseMsgService"
    private var title = ""
    private var details = ""
    private var type = ""


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "token: $token")
        DeviceTokenManager().saveToken(token, this)
        val intent = Intent(UPDATE_PROFILE_BROADCAST)
        LocalBroadcastManager.getInstance(AppController.instance.applicationContext).sendBroadcast(intent)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e(TAG, "Remote Message: " + remoteMessage.data.toString())
        val id = System.currentTimeMillis().toInt()

        if (remoteMessage.notification != null) {
            title = remoteMessage.notification?.title + ""
            details = remoteMessage.notification!!.body.toString()
        }
        // sendNotification(id, title, details, type)
    }


}
