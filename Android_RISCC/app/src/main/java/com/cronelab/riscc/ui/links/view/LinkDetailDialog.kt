package com.cronelab.riscc.ui.links.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cronelab.riscc.R
import com.cronelab.riscc.support.data.database.table.DBLinks
import com.cronelab.riscc.ui.WebViewer
import kotlinx.android.synthetic.main.activity_links_detail.*
import kotlinx.android.synthetic.main.link_detail_popup.*
import kotlinx.android.synthetic.main.link_detail_popup.linkIV
import kotlinx.android.synthetic.main.link_detail_popup.titleTV

class LinkDetailDialog : DialogFragment() {
    companion object {
        const val TAG = "LinkDetailDialog"
        private const val KEY_LINKS = "DBLinks"

        fun newInstance(dbLinks: DBLinks): LinkDetailDialog {
            val args = Bundle()
            args.putSerializable(KEY_LINKS, dbLinks)
            val fragment = LinkDetailDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.link_detail_popup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        initListeners()
    }

    private fun setData() {
        val links: DBLinks = arguments?.get(KEY_LINKS) as DBLinks
        titleTV.text = links.title
        detailTV.text = links.description
        emailTV.text = links.emailAddress
        contactTV.text = links.contactNumber
        if (links.url.isNotEmpty()) {
            linkIV.visibility = View.VISIBLE
            linkIV.isEnabled = true
        } else {
            linkIV.visibility = View.INVISIBLE
            linkIV.isEnabled = false
        }
        linkIV.setOnClickListener {
            val intent = Intent(requireContext(), WebViewer::class.java)
            intent.putExtra("Url", links.url)
            startActivity(intent)
            dismiss()
        }

    }

    private fun initListeners() {
        closeTV.setOnClickListener {
            dismiss()
        }


    }
}