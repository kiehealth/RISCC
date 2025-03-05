package com.cronelab.riscc.ui.notification

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.cronelab.riscc.R
import com.cronelab.riscc.support.analytics.AnalyticsBroadcastManager
import com.cronelab.riscc.support.analytics.AppAnalyticsViewModel
import com.cronelab.riscc.support.analytics.AppAnalyticsViewModelFactory
import com.cronelab.riscc.support.common.baseClass.BaseActivity
import com.cronelab.riscc.support.common.utils.DateUtils
import com.cronelab.riscc.support.constants.NOTIFICATION_DETAIL_IN
import com.cronelab.riscc.support.constants.NOTIFICATION_DETAIL_OUT
import com.cronelab.riscc.support.data.database.table.DBNotification
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.singleton.User
import kotlinx.android.synthetic.main.activity_notification_detail.*
import org.kodein.di.generic.instance

class NotificationDetail : BaseActivity() {

    private var dbUser: DBUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_detail)
        dbUser = User.getInstance(this).user
        val notification: DBNotification = intent.getSerializableExtra("notificationObj") as DBNotification
        titleTV.text = notification.title
        descriptionTV.text = notification.description
        dateTV.text = DateUtils.formatDate(notification.dateTime.toLong(), DateUtils.format5)

    }

    fun navigateBack(view: View) {
        finish()
    }

    override fun onResume() {
        super.onResume()
        postAnalytics(NOTIFICATION_DETAIL_IN)
    }

    override fun onPause() {
        super.onPause()
        postAnalytics(NOTIFICATION_DETAIL_OUT)
    }

    override fun onDestroy() {
        super.onDestroy()
        AnalyticsBroadcastManager().sendPostAnalyticsBroadcast(NOTIFICATION_DETAIL_OUT)
    }

    private fun postAnalytics(key: String) {
        val appAnalyticsViewModelFactory: AppAnalyticsViewModelFactory by instance()
        val appAnalyticsViewModel: AppAnalyticsViewModel = ViewModelProviders.of(this, appAnalyticsViewModelFactory).get(AppAnalyticsViewModel::class.java)
        appAnalyticsViewModel.getAppAnalytics(key)
        appAnalyticsViewModel.appAnalyticsType.observe(this, {
            if (it != null && it.isNotEmpty()) {
                appAnalyticsViewModel.resetObserver()
                appAnalyticsViewModel.appAnalyticsType.removeObserver { }
                if (dbUser != null) {
                    appAnalyticsViewModel.sendAppAnalytics(it[0], dbUser!!)
                }
            }
        })
    }
}