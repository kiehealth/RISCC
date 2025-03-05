package com.cronelab.riscc.ui.note

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.cronelab.riscc.R
import com.cronelab.riscc.support.analytics.AnalyticsBroadcastManager
import com.cronelab.riscc.support.analytics.AppAnalyticsViewModel
import com.cronelab.riscc.support.analytics.AppAnalyticsViewModelFactory
import com.cronelab.riscc.support.common.baseClass.BaseActivity
import com.cronelab.riscc.support.common.showToast
import com.cronelab.riscc.support.constants.NOTE_DETAIL_IN
import com.cronelab.riscc.support.constants.NOTE_DETAIL_OUT
import com.cronelab.riscc.support.data.database.table.DBNote
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.singleton.User
import com.cronelab.riscc.ui.note.model.NoteViewModel
import com.cronelab.riscc.ui.note.model.NoteViewModelFactory
import kotlinx.android.synthetic.main.fragment_edit_note.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class EditNote : BaseActivity(), KodeinAware, View.OnClickListener {

    private val TAG = "EditNote"

    override val kodein by kodein()
    private lateinit var noteViewModel: NoteViewModel
    private val noteViewModelFactory: NoteViewModelFactory by instance()

    private var dbUser: DBUser? = null
    private lateinit var dbNote: DBNote
    private var isSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_edit_note)
        noteViewModel = ViewModelProviders.of(this, noteViewModelFactory).get(NoteViewModel::class.java)
        dbUser = User.getInstance(this).user
        dbNote = intent.getSerializableExtra("dbNote") as DBNote
        titleET.setText(dbNote.title)
        descriptionET.setText(dbNote.description)
        initListener()
    }

    override fun onResume() {
        super.onResume()
        postAnalytics(NOTE_DETAIL_IN)
    }

    override fun onPause() {
        super.onPause()
        postAnalytics(NOTE_DETAIL_OUT)
    }

    override fun onDestroy() {
        super.onDestroy()
        AnalyticsBroadcastManager().sendPostAnalyticsBroadcast(NOTE_DETAIL_OUT)
    }

    private fun initListener() {
        saveNoteBtn.setOnClickListener(this)
    }

    fun navigateBack(view: View) {
        onBackPressed()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.saveNoteBtn -> {
                dbUser?.let {
                    saveNote(it)
                }
            }
        }
    }

    private fun saveNote(user: DBUser) {
        if (!titleET.text.toString().equals(dbNote.title, ignoreCase = true) || !descriptionET.text.toString().equals(dbNote.description, ignoreCase = true)) {
            dbNote.title = titleET.text.toString()
            dbNote.description = descriptionET.text.toString()
            dbNote.isSynced = false
            noteViewModel.updateNoteInDB(dbNote)
            noteViewModel.updatedNoteList.observe(this, {
                if (it != null) {
                    isSaved = true
                    noteViewModel.updateNoteInServer(user, dbNote)
                }
            })
            noteViewModel.noteUpdateResponse.observe(this, {
                showToast(getString(R.string.noteUpdated))//since updated in database, successful message is shown
                if (it.error == null) {
                    onBackPressed()
                } else {
                    showToast(it.message)
                    if (it.message.contains("Failed to connect")) { //unable to connect to server
                        onBackPressed()
                    }
                }
            })
        }
    }

    override fun onBackPressed() {
        if (isSaved) {
            super.onBackPressed()
        } else if (titleET.text.toString().equals(dbNote.title, ignoreCase = true)
            && descriptionET.text.toString().equals(dbNote.description, ignoreCase = true)
        ) {
            super.onBackPressed()
        } else {
            val alertDialogBuilder = AlertDialog.Builder(this)
            with(alertDialogBuilder) {
                setTitle(getString(R.string.noteWillBeDiscarded))
                setCancelable(false)
                setPositiveButton(getString(R.string.exit)) { dialog, which ->
                    dialog.dismiss()
                    super.onBackPressed()
                }
                setNegativeButton(getString(R.string.no)) { dialog, which ->
                    dialog.dismiss()
                }
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
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