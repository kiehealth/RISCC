package com.cronelab.riscc.support.analytics

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.cronelab.riscc.support.AppController
import com.cronelab.riscc.support.constants.ANALYTICS_BROADCAST

class AnalyticsBroadcastManager {
    public fun sendPostAnalyticsBroadcast(key: String) {
        val intent = Intent(ANALYTICS_BROADCAST)
        intent.putExtra("key", key)
        LocalBroadcastManager.getInstance(AppController.instance.applicationContext).sendBroadcast(intent)
    }
}