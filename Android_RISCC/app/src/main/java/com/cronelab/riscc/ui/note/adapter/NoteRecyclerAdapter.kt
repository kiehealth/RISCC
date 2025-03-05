package com.cronelab.riscc.ui.note.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cronelab.riscc.R
import com.cronelab.riscc.support.common.utils.DateUtils
import com.cronelab.riscc.support.data.database.table.DBNote
import kotlinx.android.synthetic.main.row_links.view.descriptionTV
import kotlinx.android.synthetic.main.row_note.view.*

class NoteRecyclerAdapter(private var noteList: List<DBNote>) : RecyclerView.Adapter<NoteRecyclerAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    lateinit var noteItemClickCallback: NoteItemClickCallback

    fun setOnItemClick(noteItemClickCallback: NoteItemClickCallback) {
        this.noteItemClickCallback = noteItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_note, parent, false))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.itemView.titleTV.text = note.title
        holder.itemView.descriptionTV.text = note.description
        holder.itemView.dateTV.text = DateUtils.formatDate(note.createdDateTime?.toLong(), DateUtils.format2)
        holder.itemView.setOnClickListener {
            noteItemClickCallback.viewNoteDetail(note)
        }
        holder.itemView.editIV.setOnClickListener {
            noteItemClickCallback.editNote(position, note)
        }
        holder.itemView.deleteIV.setOnClickListener {
            noteItemClickCallback.deleteNote(position, note)
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }


    public fun updateNotesList(noteList: List<DBNote>) {
        this.noteList = noteList
        notifyDataSetChanged()
    }


    interface NoteItemClickCallback {
        fun deleteNote(position: Int, note: DBNote)
        fun viewNoteDetail(note: DBNote)
        fun editNote(position: Int, note: DBNote)
    }

}