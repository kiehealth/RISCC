package com.cronelab.riscc.support.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.cronelab.riscc.ui.note.model.NoteViewModel
import com.cronelab.riscc.ui.note.model.NoteViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class NoteSyncService : Service(), KodeinAware {

    override val kodein by kodein()
    private val noteViewModelFactory: NoteViewModelFactory by instance()
    private lateinit var noteViewModel:NoteViewModel

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}