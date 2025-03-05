package com.cronelab.riscc.ui.links.view

import android.annotation.SuppressLint
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
import com.cronelab.riscc.support.common.helper.PaginationScrollListener
import com.cronelab.riscc.support.constants.LINK_IN
import com.cronelab.riscc.support.constants.LINK_OUT
import com.cronelab.riscc.support.data.database.table.DBLinks
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.singleton.User
import com.cronelab.riscc.ui.links.adapter.LinksRecyclerAdapter
import com.cronelab.riscc.ui.links.model.LinksViewModel
import com.cronelab.riscc.ui.links.model.LinksViewModelFactory
import kotlinx.android.synthetic.main.fragment_links.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import kotlin.collections.ArrayList

class Links : Fragment(), KodeinAware {

    val TAG = "Links"
    override val kodein by kodein()

    private lateinit var linksViewModel: LinksViewModel
    private val linksViewModelFactory: LinksViewModelFactory by instance()
    var dbUser: DBUser? = null

    private var linksList = ArrayList<DBLinks>()
    private val linksRecyclerAdapter = LinksRecyclerAdapter(linksList)

    private var isLoading = false
    private var size = 6
    private var initialPage = 0
    private var currentPage = initialPage
    private var isMoreToLoad = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_links, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linksViewModel = ViewModelProviders.of(requireActivity(), linksViewModelFactory).get(LinksViewModel::class.java)
        dbUser = User.getInstance(requireContext()).user
        configureLinksRecyclerView()
        initListener()
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        getLinksFromServer()
        postAnalytics(LINK_IN)
    }

    override fun onPause() {
        super.onPause()
        postAnalytics(LINK_OUT)
    }

    override fun onDestroy() {
        super.onDestroy()
        AnalyticsBroadcastManager().sendPostAnalyticsBroadcast(LINK_OUT)
    }

    private fun configureLinksRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        linksRecyclerView.layoutManager = layoutManager
        linksRecyclerView.setHasFixedSize(true)
        linksRecyclerView.adapter = linksRecyclerAdapter
        getLinksFromDB()

        linksRecyclerView.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                if (lastVisiblePosition == linksRecyclerAdapter.itemCount - 1 && !isLoading && isMoreToLoad) {
                    isLoading = true
                    currentPage++
                    dbUser?.let { user ->
                        loadingMoreLayout.visibility = View.VISIBLE
                        linksViewModel.getLinksFromServer(user, currentPage, size)
                    }
                }
            }

            override fun configureIndicatorColor(firstVisibleItemPosition: Int) {

            }

            override fun getTotalPageCount(): Int {
                return 0
            }

            override fun isLastPage(): Boolean {
                return false
            }

            override fun isLoading(): Boolean {
                return false
            }
        })

        linksRecyclerAdapter.setOnItemClick(object : ClickCallback {
            override fun callback(position: Int, data: Any) {
                val links: DBLinks = data as DBLinks
                val intent = Intent(requireContext(), LinksDetail::class.java)
                intent.putExtra("linksObj", links)
                // startActivity(intent)
                LinkDetailDialog.newInstance(links).show(childFragmentManager, LinkDetailDialog.TAG)
            }
        })
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
            getLinksFromServer()
        }
    }

    private fun dismissSwipeRefreshing() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    @SuppressLint("DefaultLocale")
    fun filter(searchText: String) {
        linksViewModel.getLinksFromDB()
        val links: ArrayList<DBLinks> = ArrayList()
        linksViewModel.linksList.observe(requireActivity(), Observer {
            if (it.isNotEmpty()) {
                linksViewModel.linksList.removeObserver {}
                for (link in it) {
                    if (link.title.toLowerCase().contains(searchText.toLowerCase().trim()) || link.description.toLowerCase().contains(searchText.toLowerCase().trim())) {
                        links.add(link)
                    }
                }
                linksRecyclerAdapter.updateLinksList(links)
            }
        })
    }

    private fun getLinksFromDB() {
        linksViewModel.getLinksFromDB()
        linksViewModel.linksList.observe(requireActivity(), {
            if (it.isNotEmpty()) {
                linksViewModel.linksList.removeObserver {}
                linksRecyclerAdapter.updateLinksList(it)
            }
        })
    }

    private fun getLinksFromServer() {
        dbUser?.let { user ->
            swipeRefreshLayout.isRefreshing = true
            linksViewModel.getLinksFromServer(user, currentPage, size)

        }
    }

    private fun initObserver() {
        linksViewModel.linksResponse.observe(requireActivity(), Observer {
            if (isVisible) {
                dismissSwipeRefreshing()
                linksViewModel.linksResponse.removeObserver {}
                if (it.error != null) {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                } else {
                    isLoading = false
                    loadingMoreLayout.visibility = View.GONE
                    val newLinksList = it.links
                    linksList.addAll(newLinksList)
                    linksRecyclerAdapter.updateLinksList(linksList)
                    isMoreToLoad = it.endPosition < it.totalRecord
                }
            }
        })
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