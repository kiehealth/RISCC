package com.cronelab.riscc.ui.notification

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cronelab.riscc.R
import com.cronelab.riscc.support.analytics.AnalyticsBroadcastManager
import com.cronelab.riscc.support.analytics.AppAnalyticsViewModel
import com.cronelab.riscc.support.analytics.AppAnalyticsViewModelFactory
import com.cronelab.riscc.support.callback.ClickCallback
import com.cronelab.riscc.support.constants.NOTIFICATION_IN
import com.cronelab.riscc.support.constants.NOTIFICATION_OUT
import com.cronelab.riscc.support.data.database.table.DBNotification
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.singleton.User
import com.cronelab.riscc.ui.notification.adapter.NotificationRecyclerAdapter
import com.cronelab.riscc.ui.notification.model.NotificationVMFactory
import com.cronelab.riscc.ui.notification.model.NotificationViewModel
import kotlinx.android.synthetic.main.notification_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class Notification : Fragment(), KodeinAware {
    val TAG = "Notification"
    override val kodein by kodein()

    private var dbUser: DBUser? = null

    private lateinit var notificationVM: NotificationViewModel
    private val notificationVMFactory: NotificationVMFactory by instance()

    private var notificationList: List<DBNotification> = ArrayList()
    private val notificationRecyclerAdapter = NotificationRecyclerAdapter(notificationList)

    private lateinit var appAnalyticsViewModel: AppAnalyticsViewModel
    private val appAnalyticsViewModelFactory: AppAnalyticsViewModelFactory by instance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.notification_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationVM = ViewModelProviders.of(requireActivity(), notificationVMFactory).get(NotificationViewModel::class.java)
        appAnalyticsViewModel = ViewModelProviders.of(requireActivity(), appAnalyticsViewModelFactory).get(AppAnalyticsViewModel::class.java)
        dbUser = User.getInstance(requireContext()).user
        configureNotificationRecyclerview()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        getNotificationFromServer()
        postAnalytics(NOTIFICATION_IN)
    }

    override fun onPause() {
        super.onPause()
        postAnalytics(NOTIFICATION_OUT)
    }

    override fun onDestroy() {
        super.onDestroy()
        AnalyticsBroadcastManager().sendPostAnalyticsBroadcast(NOTIFICATION_OUT)
    }


    private fun initListener() {
        searchET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })

        swipeRefreshLayout.setOnRefreshListener {
            getNotificationFromServer()
        }
    }

    fun filter(searchText: String) {
        notificationVM.getNotificationFromDB()
        val notificationList = ArrayList<DBNotification>()
        notificationVM.notificationList.observe(requireActivity(), {
            if (it.isNotEmpty()) {
                notificationVM.notificationList.removeObserver { }
                for (notification in it) {
                    if (notification.title.toLowerCase().contains(searchText.toLowerCase().trim()) || notification.description.toLowerCase().contains(searchText.toLowerCase().trim())) {
                        notificationList.add(notification)
                    }
                }
                notificationRecyclerAdapter.updateNotificationList(notificationList)
            }
        })
    }


    private fun configureNotificationRecyclerview() {
        val layoutManager = LinearLayoutManager(requireContext())
        notificationRecyclerView.layoutManager = layoutManager
        notificationRecyclerView.setHasFixedSize(true)
        notificationRecyclerView.adapter = notificationRecyclerAdapter
        getNotificationFromDB()
        notificationRecyclerAdapter.setOnItemClick(object : ClickCallback {
            override fun callback(position: Int, data: Any) {
                val notification = data as DBNotification
                val intent = Intent(requireContext(), NotificationDetail::class.java)
                intent.putExtra("notificationObj", notification)
                //startActivity(intent)
                NotificationDetailDialog.newInstance(notification).show(childFragmentManager, NotificationDetailDialog.TAG)
            }
        })
    }

    private fun getNotificationFromDB() {
        notificationVM.getNotificationFromDB()
        notificationVM.notificationList.observe(requireActivity(), Observer {
            if (it.isNotEmpty()) {
                notificationVM.notificationList.removeObserver {}
                notificationRecyclerAdapter.updateNotificationList(it)
            }
        })
    }

    private fun dismissSwipeRefreshing() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
    }


    private fun getNotificationFromServer() {
        swipeRefreshLayout.isRefreshing = true
        dbUser?.let {
            notificationVM.getNotificationFromServer(it)
            notificationVM.notificationResponse.observe(requireActivity(), Observer {
                if (isVisible) {
                    dismissSwipeRefreshing()
                    notificationVM.notificationResponse.removeObserver { }
                    if (it.error != null) {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    } else {
                        notificationRecyclerAdapter.updateNotificationList(it.notificationList)
                    }
                }
            })
        }
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