package com.cronelab.riscc.ui.notification.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cronelab.riscc.R
import com.cronelab.riscc.support.callback.ClickCallback
import com.cronelab.riscc.support.common.utils.DateUtils
import com.cronelab.riscc.support.data.database.table.DBNotification
import kotlinx.android.synthetic.main.row_notificaion.view.*

class NotificationRecyclerAdapter(var notificationList: List<DBNotification>) : RecyclerView.Adapter<NotificationRecyclerAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var clickCallback: ClickCallback

    public fun setOnItemClick(clickCallback: ClickCallback) {
        this.clickCallback = clickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_notificaion, parent, false))
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]
        holder.itemView.titleTV.text = notification.title
        holder.itemView.descriptionTV.text = notification.description
        holder.itemView.dateTV.text = DateUtils.formatDate(notification.dateTime.toLong(), DateUtils.format5)
        holder.itemView.setOnClickListener {
            clickCallback.callback(position, notification)
        }
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    fun updateNotificationList(notificationList: List<DBNotification>) {
        this.notificationList = notificationList
        notifyDataSetChanged()
    }


}