package com.cronelab.riscc.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cronelab.riscc.R
import com.cronelab.riscc.support.data.database.table.DBNote
import kotlinx.android.synthetic.main.note_detail_popup.*


class NoteDetailDialog : DialogFragment() {

    companion object {
        const val TAG = "NoteDetailDialog"
        private const val KEY_NOTE = "DBNote"

        fun newInstance(dbNote: DBNote): NoteDetailDialog {
            val args = Bundle()
            args.putSerializable(KEY_NOTE, dbNote)
            val fragment = NoteDetailDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.note_detail_popup, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        initListeners()
    }

    private fun setData() {
        val links: DBNote = arguments?.get(KEY_NOTE) as DBNote
        titleTV.text = links.title
        detailTV.text = links.description
    }

    private fun initListeners() {
        closeTV.setOnClickListener {
            dismiss()
        }
    }
}