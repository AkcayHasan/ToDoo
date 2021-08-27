package com.hasanakcay.todoo.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hasanakcay.todoo.databinding.ItemNoteBinding
import com.hasanakcay.todoo.data.db.Note
import com.hasanakcay.todoo.util.AdapterClickListener

class NoteListAdapter(
    private val noteList: MutableList<Note>,
    private val adapterClickListener: AdapterClickListener
) :
    RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder>() {

    class NoteListViewHolder(private val itemBinding: ItemNoteBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(note: Note) {
            itemBinding.recyclerHeader.text = note.header
            itemBinding.recyclerDate.text = note.date

            when (note.priorityId) {
                "1" -> { itemBinding.viewColor.setBackgroundColor(Color.RED) }
                "2" -> { itemBinding.viewColor.setBackgroundColor(Color.rgb(255, 165, 0)) }
                else -> itemBinding.viewColor.setBackgroundColor(Color.YELLOW)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        return NoteListViewHolder(
            ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        val noteInstance: Note = noteList[position]
        holder.bind(noteInstance)

        holder.itemView.setOnClickListener {
            adapterClickListener.onClick(noteList[position].id)
        }
    }

    override fun getItemCount() = noteList.size
}