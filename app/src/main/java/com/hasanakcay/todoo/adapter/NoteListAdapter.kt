package com.hasanakcay.todoo.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hasanakcay.todoo.R
import com.hasanakcay.todoo.model.Note
import com.hasanakcay.todoo.view.NoteDetailActivity
import kotlinx.android.synthetic.main.note_recycler_row.view.*

class NoteListAdapter(val noteList: MutableList<Note>, contxt: Context) :
    RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder>() {

    val context = contxt

    inner class NoteListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        return NoteListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_recycler_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        holder.itemView.apply {
            recycler_header.text = noteList.get(position).header
            recycler_date.text = noteList.get(position).date

            if (noteList.get(position).priorityId.equals("1")) {
                view_color.setBackgroundColor(Color.RED)
            } else if (noteList.get(position).priorityId.equals("2")) {
                view_color.setBackgroundColor(Color.rgb(255, 165, 0))
            } else {
                view_color.setBackgroundColor(Color.YELLOW)
            }
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, NoteDetailActivity::class.java)
            intent.putExtra("selectedNote", noteList.get(position).id)
            (context as Activity).startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }
}