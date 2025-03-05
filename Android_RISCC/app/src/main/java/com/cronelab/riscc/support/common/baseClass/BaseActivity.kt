package com.cronelab.riscc.support.common.baseClass

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.cronelab.riscc.support.analytics.AppAnalyticsViewModel
import com.cronelab.riscc.support.analytics.AppAnalyticsViewModelFactory
import com.cronelab.riscc.support.constants.ANALYTICS_BROADCAST
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.singleton.User
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModel
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModelFactory
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

open class BaseActivity : AppCompatActivity(), KodeinAware {

    private val TAG = "BaseActivity"
    override val kodein by kodein()

    private val userViewModelFactory: UserViewModelFactory by instance()
    private lateinit var userViewModel: UserViewModel
    private var dbUser: DBUser? = null

    private lateinit var appAnalyticsViewModel: AppAnalyticsViewModel
    private val appAnalyticsViewModelFactory: AppAnalyticsViewModelFactory by instance()

    private val analyticsBroadcastListener = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val key = it.getStringExtra("key")
                Log.d(TAG, "Key : $key")
                key?.let { it1 ->
                    postAnalytics(it1)
                }

            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProviders.of(this, userViewModelFactory).get(UserViewModel::class.java)
        appAnalyticsViewModel = ViewModelProviders.of(this, appAnalyticsViewModelFactory).get(AppAnalyticsViewModel::class.java)
        try {
            dbUser = User.getInstance(this).user
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this@BaseActivity).registerReceiver(analyticsBroadcastListener, IntentFilter(ANALYTICS_BROADCAST))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this@BaseActivity).unregisterReceiver(analyticsBroadcastListener)
    }


    private fun postAnalytics(key: String) {
        appAnalyticsViewModel.getAppAnalytics(key)
        appAnalyticsViewModel.appAnalyticsType.observe(this) {
            if (it != null && it.isNotEmpty()) {
                Log.d(TAG, "All Analytics data: ${Gson().toJson(it)}")
                appAnalyticsViewModel.resetObserver()
                appAnalyticsViewModel.appAnalyticsType.removeObserver { }
                if (dbUser != null) {
                    appAnalyticsViewModel.sendAppAnalytics(it[0], dbUser!!)
                }
            }
        }
    }


//    fun setWindowFullScreen(window: Window) {
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
//    }
//
//    fun setStatusBarTextColor(widow: Window) {
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//                window.statusBarColor = ContextCompat.getColor(this, R.color.backgroundColor)
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//    }

}