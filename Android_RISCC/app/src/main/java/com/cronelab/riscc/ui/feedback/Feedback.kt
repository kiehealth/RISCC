package com.cronelab.riscc.ui.feedback

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cronelab.riscc.R
import com.cronelab.riscc.support.analytics.AnalyticsBroadcastManager
import com.cronelab.riscc.support.analytics.AppAnalyticsViewModel
import com.cronelab.riscc.support.analytics.AppAnalyticsViewModelFactory
import com.cronelab.riscc.support.constants.FEEDBACK_IN
import com.cronelab.riscc.support.constants.FEEDBACK_OUT
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.singleton.User
import com.cronelab.riscc.ui.feedback.model.FeedbackViewModel
import com.cronelab.riscc.ui.feedback.model.FeedbackViewModelFactory
import com.cronelab.riscc.ui.progressDialog.ProgressDialog
import kotlinx.android.synthetic.main.activity_feedback.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class Feedback : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private lateinit var feedbackViewModel: FeedbackViewModel
    private val feedbackViewModelFactory: FeedbackViewModelFactory by instance()

    var dbUser: DBUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        feedbackViewModel = ViewModelProviders.of(this, feedbackViewModelFactory).get(FeedbackViewModel::class.java)
        dbUser = User.getInstance(this).user
        submitBtn.setOnClickListener {
            submitFeedback()
        }
    }

    private fun submitFeedback() {
        dbUser?.let { user ->
            val progressDialog = ProgressDialog.loadingProgressDialog(this, getString(R.string.submittingFeedback))
            progressDialog.show()
            feedbackViewModel.submitFeedback(user, titleET.text.toString(), feedbackET.text.toString())
            feedbackViewModel.feedbackResponse.observe(this, Observer {
                if (!isFinishing) {
                    progressDialog.dismiss()
                    feedbackViewModel.feedbackResponse.removeObserver { }
                    if (it.error != null) {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    } else {
                        titleET.setText("")
                        feedbackET.setText("")
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    fun navigateBack(view: View) {
        onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        postAnalytics(FEEDBACK_IN)
    }

    override fun onPause() {
        super.onPause()
        postAnalytics(FEEDBACK_OUT)
    }

    override fun onDestroy() {
        super.onDestroy()
        AnalyticsBroadcastManager().sendPostAnalyticsBroadcast(FEEDBACK_OUT)
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