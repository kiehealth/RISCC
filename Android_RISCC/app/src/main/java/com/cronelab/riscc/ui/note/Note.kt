package com.cronelab.riscc.ui.note

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.cronelab.riscc.R
import com.cronelab.riscc.support.analytics.AnalyticsBroadcastManager
import com.cronelab.riscc.support.analytics.AppAnalyticsViewModel
import com.cronelab.riscc.support.analytics.AppAnalyticsViewModelFactory
import com.cronelab.riscc.support.common.baseClass.BaseFragment
import com.cronelab.riscc.support.common.helper.PaginationScrollListener
import com.cronelab.riscc.support.common.showToast
import com.cronelab.riscc.support.constants.NOTE_IN
import com.cronelab.riscc.support.constants.NOTE_OUT
import com.cronelab.riscc.support.data.database.table.DBNote
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.singleton.User
import com.cronelab.riscc.ui.links.view.LinkDetailDialog
import com.cronelab.riscc.ui.note.adapter.NoteRecyclerAdapter
import com.cronelab.riscc.ui.note.model.NoteViewModel
import com.cronelab.riscc.ui.note.model.NoteViewModelFactory
import com.cronelab.riscc.ui.progressDialog.ProgressDialog
import kotlinx.android.synthetic.main.fragment_note.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class Note : BaseFragment(), KodeinAware {

    val TAG = "Note"
    override val kodein by kodein()

    private lateinit var noteViewModel: NoteViewModel
    private val noteViewModelFactory: NoteViewModelFactory by instance()

    //    private var noteList: List<DBNote> = ArrayList()
    private var noteList = ArrayList<DBNote>()
    private var noteRecyclerAdapter = NoteRecyclerAdapter(noteList)

    private lateinit var navController: NavController
    private var dbUser: DBUser? = null

    private var isLoading = false
    private var size = 6
    private var initialPage = 0
    private var currentPage = initialPage
    private var isMoreToLoad = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = ViewModelProviders.of(requireActivity(), noteViewModelFactory).get(NoteViewModel::class.java)
        dbUser = User.getInstance(requireContext()).user
        configureNoteRecyclerView()
        navController = Navigation.findNavController(requireView())
        initListener()
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        updateNoteFromDB()
        getNotesFromServer()
        postAnalytics(NOTE_IN)
    }

    override fun onPause() {
        postAnalytics(NOTE_OUT)
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Note", "on Destroy")
//        postAnalytics(NOTE_OUT) //this does not work on home button pressed
        AnalyticsBroadcastManager().sendPostAnalyticsBroadcast(NOTE_OUT)
    }


    private fun initListener() {
        addNoteFab.setOnClickListener {
            navController.navigate(R.id.action_navigation_note_to_addNote)
        }
        searchET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })

        swipeRefreshLayout.setOnRefreshListener {
            //currentPage = 0
            getNotesFromServer()
        }
    }

    private fun filter(searchText: String) {
        noteViewModel.getNotesFromDB()
        val noteList = ArrayList<DBNote>()
        noteViewModel.noteList.observe(requireActivity(), {
            if (it.isNotEmpty()) {
                noteViewModel.noteList.removeObserver { }
                for (note in it) {
                    if (note.title.toLowerCase().contains(searchText.toLowerCase().trim()) || note.description.toLowerCase().contains(searchText.toLowerCase().trim())) {
                        noteList.add(note)
                    }
                }
                noteRecyclerAdapter.updateNotesList(noteList)
            }
        })
    }

    private fun configureNoteRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        noteRecyclerView.layoutManager = layoutManager
        noteRecyclerView.setHasFixedSize(true)
        noteRecyclerView.adapter = noteRecyclerAdapter
        noteRecyclerView.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                if (lastVisiblePosition == noteRecyclerAdapter.itemCount - 1 && !isLoading && isMoreToLoad) {
                    isLoading = true
                    currentPage++
                    dbUser?.let { user ->
                        loadingMoreLayout.visibility = View.VISIBLE
                        noteViewModel.getNotes(user, currentPage, size)
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
        noteRecyclerAdapter.noteItemClickCallback = object : NoteRecyclerAdapter.NoteItemClickCallback {

            override fun editNote(position: Int, note: DBNote) {
                val intent = Intent(requireContext(), EditNote::class.java)
                intent.putExtra("dbNote", note)
                startActivity(intent)
            }

            override fun viewNoteDetail(note: DBNote) {
                val intent = Intent(requireContext(), EditNote::class.java)
                intent.putExtra("dbNote", note)
                //startActivity(intent)
                NoteDetailDialog.newInstance(note).show(childFragmentManager, NoteDetailDialog.TAG)

            }

            override fun deleteNote(position: Int, note: DBNote) {
                dbUser?.let { user ->
                    val progressDialog = ProgressDialog.loadingProgressDialog(requireContext(), getString(R.string.delete_note_msg))
                    progressDialog.show()
                    noteViewModel.deleteNote(user, note)
                    noteViewModel.noteDeleteResponse.observe(requireActivity(), {
                        if (isVisible) {
                            progressDialog.dismiss()
                            noteViewModel.noteDeleteResponse.removeObserver { }
                            if (it != null) {
                                requireActivity().showToast(it.message)
                                if (it.error == null) {
                                    getNotesFromServer()
                                }
                            }
                        }
                    })
                }
            }
        }
    }

    private fun dismissSwipeRefreshing() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun getNotesFromServer() {
        dbUser?.let { user ->
            swipeRefreshLayout.isRefreshing = true
            noteViewModel.getNotes(user, currentPage, size)
            /* noteViewModel.noteResponse.observe(requireActivity(), Observer {
                 if (isVisible) {
                     dismissSwipeRefreshing()
                     if (it.error == null) {
                         noteRecyclerAdapter.updateNotesList(it.noteList)
                     }
                 }
             })*/
        }
    }

    private fun initObserver() {
        noteViewModel.noteResponse.observe(requireActivity(), Observer {
            if (isVisible) {
                dismissSwipeRefreshing()
                if (it.error == null) {
                    isLoading = false
                    loadingMoreLayout.visibility = View.GONE
                    val newNoteList = it.noteList
                    noteList.addAll(newNoteList)
                    noteRecyclerAdapter.updateNotesList(noteList)
                    Log.w(TAG, "endPosition: ${it.endPosition} \t totalRecord : ${it.totalRecord}")
                    //isMoreToLoad = (currentPage < it.totalPage)
                    isMoreToLoad = it.endPosition < it.totalRecord
                }
            }
        })
    }

    private fun updateNoteFromDB() {
        noteViewModel.getNotesFromDB()
        noteViewModel.noteList.observe(requireActivity(), {
            if (isVisible) {
                noteViewModel.noteList.removeObserver { }
                if (it.isNotEmpty()) {
                    noteRecyclerAdapter.updateNotesList(it)
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