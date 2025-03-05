package com.cronelab.riscc.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cronelab.riscc.R
import com.cronelab.riscc.support.common.utils.DateUtils
import com.cronelab.riscc.support.data.database.table.DBNotification
import kotlinx.android.synthetic.main.activity_notification_detail.*
import kotlinx.android.synthetic.main.notification_detail_popup.*
import kotlinx.android.synthetic.main.notification_detail_popup.dateTV
import kotlinx.android.synthetic.main.notification_detail_popup.titleTV

class NotificationDetailDialog : DialogFragment() {
    companion object {
        const val TAG = "NotificationDetailDialog"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_DETAIL = "KEY_DETAIL"
        private const val KEY_DATE = "KEY_DATE"

        fun newInstance(title: String, detail: String): NotificationDetailDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_DETAIL, KEY_DETAIL)
            val fragment = NotificationDetailDialog()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(notificationObj: DBNotification): NotificationDetailDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, notificationObj.title)
            args.putString(KEY_DETAIL, notificationObj.description)
            args.putString(KEY_DATE, notificationObj.dateTime)
            val fragment = NotificationDetailDialog()
            fragment.arguments = args
            return fragment
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.notification_detail_popup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        initListeners()
    }

    private fun setData() {
        titleTV.text = arguments?.getString(KEY_TITLE)
        detailTV.text = arguments?.getString(KEY_DETAIL)
        dateTV.text = DateUtils.formatDate(arguments?.getString(KEY_DATE)!!.toLong(), DateUtils.format5)
    }

    private fun initListeners() {
        closeTV.setOnClickListener {
            dismiss()
        }
    }
}