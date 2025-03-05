package com.cronelab.riscc.ui.links.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cronelab.riscc.R
import com.cronelab.riscc.support.callback.ClickCallback
import com.cronelab.riscc.support.data.database.table.DBLinks
import com.cronelab.riscc.ui.WebViewer
import kotlinx.android.synthetic.main.row_links.view.*

class LinksRecyclerAdapter(private var linksList: List<DBLinks>) : RecyclerView.Adapter<LinksRecyclerAdapter.LinksViewHolder>() {

    class LinksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var clickCallback: ClickCallback

    fun setOnItemClick(clickCallback: ClickCallback) {
        this.clickCallback = clickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinksViewHolder {
        return LinksViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_links, parent, false))
    }

    override fun onBindViewHolder(holder: LinksViewHolder, position: Int) {
        val context = holder.itemView.context
        val links = linksList[position]
        holder.itemView.toolbarTitleTV.text = links.title
        holder.itemView.descriptionTV.text = links.description
        holder.itemView.contactTV.text = links.contactNumber
        holder.itemView.emailTV.text = links.emailAddress
        if (links.contactNumber.isNullOrEmpty()) {
            holder.itemView.contactLayout.visibility = View.GONE
        } else {
            holder.itemView.contactLayout.visibility = View.VISIBLE
        }

        if (links.emailAddress.isNullOrEmpty()) {
            holder.itemView.emailTV.visibility = View.INVISIBLE
        } else {
            holder.itemView.emailTV.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {
            clickCallback.callback(position, links)
        }
        holder.itemView.linkIV.setOnClickListener {
            val intent = Intent(context, WebViewer::class.java)
            intent.putExtra("Url", links.url)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return linksList.size
    }


    public fun updateLinksList(linksList: List<DBLinks>) {
        this.linksList = linksList
        notifyDataSetChanged()
    }


}