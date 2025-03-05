package com.cronelab.riscc.support.services

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.cronelab.riscc.support.AppController
import com.cronelab.riscc.support.common.utils.Connectivity
import com.cronelab.riscc.support.constants.INTERNET_CONNECTION_STATUS

class InternetCheckerService : Service() {
    var TAG = "InternetCheckerService"
    val handler = Handler()
    var interval = 5
    override fun onCreate() {
        super.onCreate()
        Sync(call, interval * 100L)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private val call: Runnable = object : Runnable {
        override fun run() {
            val status = checkInternetConnection()
            sendBroadcastForInternetConnection(status)
            handler.postDelayed(this, (interval * 1000).toLong())
        }
    }

    private fun checkInternetConnection(): Boolean {
        return Connectivity.isConnected(AppController.instance.applicationContext)
    }

    private fun sendBroadcastForInternetConnection(status: Boolean) {
        val intent = Intent(INTERNET_CONNECTION_STATUS)
        intent.putExtra("status", status)
        //Log.w(TAG, "Connection Available: " + status)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    inner class Sync(var task: Runnable, time: Long) {
        init {
            handler.removeCallbacks(task)
            handler.postDelayed(task, time)
        }
    }
}
