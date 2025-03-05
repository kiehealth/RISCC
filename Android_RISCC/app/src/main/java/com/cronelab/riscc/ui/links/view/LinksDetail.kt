package com.cronelab.riscc.ui.links.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.cronelab.riscc.R
import com.cronelab.riscc.support.analytics.AnalyticsBroadcastManager
import com.cronelab.riscc.support.analytics.AppAnalyticsViewModel
import com.cronelab.riscc.support.analytics.AppAnalyticsViewModelFactory
import com.cronelab.riscc.support.common.baseClass.BaseActivity
import com.cronelab.riscc.support.constants.LINK_DETAIL_IN
import com.cronelab.riscc.support.constants.LINK_DETAIL_OUT
import com.cronelab.riscc.support.data.database.table.DBLinks
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.singleton.User
import com.cronelab.riscc.ui.WebViewer
import kotlinx.android.synthetic.main.activity_links_detail.*
import org.kodein.di.generic.instance

class LinksDetail : BaseActivity() {

    private var dbUser: DBUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_links_detail)
        dbUser = User.getInstance(this).user
        val links: DBLinks = intent.getSerializableExtra("linksObj") as DBLinks
        titleTV.text = links.title
        descriptionTV.text = links.description
        if(links.url.isNotEmpty()){
            linkIV.visibility = View.VISIBLE
            linkIV.isEnabled= true
        }else{
            linkIV.visibility = View.INVISIBLE
            linkIV.isEnabled= false
        }
        linkIV.setOnClickListener {
            val intent = Intent(this@LinksDetail, WebViewer::class.java)
            intent.putExtra("Url", links.url)
            startActivity(intent)
        }


    }

    fun navigateBack(view: View) {
        finish()
    }

    override fun onResume() {
        super.onResume()
        postAnalytics(LINK_DETAIL_IN)
    }

    override fun onPause() {
        super.onPause()
        postAnalytics(LINK_DETAIL_OUT)
    }

    override fun onDestroy() {
        super.onDestroy()
        AnalyticsBroadcastManager().sendPostAnalyticsBroadcast(LINK_DETAIL_OUT)
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