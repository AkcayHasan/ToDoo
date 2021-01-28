package com.hasanakcay.todoo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hasanakcay.todoo.R
import com.hasanakcay.todoo.adapter.NoteListAdapter
import com.hasanakcay.todoo.util.RealmHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var noteRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteRecyclerView = findViewById(R.id.rv_note_list)

        if (RealmHelper().getAllNote(this).size == 0) {
            empty_list_tv.visibility = View.VISIBLE
            noteRecyclerView.visibility = View.GONE
        } else {
            empty_list_tv.visibility = View.GONE
            noteRecyclerView.visibility = View.VISIBLE
            noteRecyclerView.layoutManager = LinearLayoutManager(this)
            noteRecyclerView.adapter = NoteListAdapter(RealmHelper().getAllNote(this), this)
        }
    }

    fun addNote(view: View) {
        val intent = Intent(this, NoteDetailActivity::class.java)
        startActivity(intent)
    }

}