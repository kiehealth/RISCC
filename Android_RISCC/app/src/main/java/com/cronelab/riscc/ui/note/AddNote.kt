package com.cronelab.riscc.ui.note

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import com.cronelab.riscc.R
import com.cronelab.riscc.support.common.baseClass.BaseFragment
import com.cronelab.riscc.support.constants.SharedPreferenceConstants
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.singleton.User
import com.cronelab.riscc.ui.note.model.NoteViewModel
import com.cronelab.riscc.ui.note.model.NoteViewModelFactory
import com.cronelab.riscc.ui.progressDialog.ProgressDialog
import kotlinx.android.synthetic.main.fragment_new_note.*
import org.kodein.di.generic.instance

class AddNote : BaseFragment(), View.OnClickListener {

    private var dbUser: DBUser? = null

    private lateinit var noteViewModel: NoteViewModel
    private val noteViewModelFactory: NoteViewModelFactory by instance()
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        dbUser = User.getInstance(requireContext()).user
        noteViewModel = ViewModelProviders.of(this, noteViewModelFactory).get(NoteViewModel::class.java)
    }

    private fun initUI() {
        navController = Navigation.findNavController(requireView())
        titleET.addTextChangedListener(titleTextWatcher)
        descriptionET.addTextChangedListener(descriptionTextWatcher)
        saveNoteBtn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.saveNoteBtn -> {
                saveNote()
            }
        }
    }

    private val titleTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.isNotEmpty() && !descriptionET.text.isNullOrEmpty()) {
                configureView(saveNoteBtn, ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                saveNoteBtn.isEnabled = true
            } else {
                configureView(saveNoteBtn, ContextCompat.getColor(requireContext(), R.color.grey))
                saveNoteBtn.isEnabled = false
            }
        }

    }

    private val descriptionTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.isNotEmpty() && !titleET.text.isNullOrEmpty()) {
                configureView(saveNoteBtn, ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                saveNoteBtn.isEnabled = true
            } else {
                configureView(saveNoteBtn, ContextCompat.getColor(requireContext(), R.color.grey))
                saveNoteBtn.isEnabled = false
            }
        }

    }

    private fun configureView(view: View, colorCode: Int) {
        if (view is Button) {
            val drawable = view.background as GradientDrawable
            drawable.setColor(colorCode)
        }
    }

    private fun saveNote() {
        dbUser?.let { user ->
            val progressDialog = ProgressDialog.loadingProgressDialog(requireContext(), getString(R.string.saving_note_please_wait))
            progressDialog.show()
            noteViewModel.sendNote(user, titleET.text.toString(), descriptionET.text.toString())
            noteViewModel.notePostResponse.observe(this, {
                if (isVisible) {
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), it.message + "", Toast.LENGTH_SHORT).show()
                    if (it.error == null) {
                        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(requireContext())
                        with(sharedPreference.edit()) {
                            putBoolean(SharedPreferenceConstants.UPDATE_NOTE_FROM_DB, true)
                            commit()
                        }
                        navController.popBackStack()
                    }
                }
            })


        }
    }


}