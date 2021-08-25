package com.hasanakcay.todoo.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hasanakcay.todoo.R
import com.hasanakcay.todoo.adapter.NoteListAdapter
import com.hasanakcay.todoo.base.BaseActivity
import com.hasanakcay.todoo.databinding.ActivityMainBinding
import com.hasanakcay.todoo.util.CustomClickListener
import com.hasanakcay.todoo.util.RealmHelper

class MainActivity : BaseActivity<ActivityMainBinding>(), CustomClickListener {

    // weather api çıkarılacak
    // mimari yapı belirlenecek

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/"
    }

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actions()

        if (RealmHelper().getAllNote(this).size == 0) {
            binding.emptyListTv.visibility = View.VISIBLE
            binding.rvNoteList.visibility = View.GONE
        } else {
            binding.emptyListTv.visibility = View.GONE
            binding.rvNoteList.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter =
                    NoteListAdapter(RealmHelper().getAllNote(this@MainActivity), this@MainActivity)
            }
        }
    }

    private fun actions() {
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, NoteDetailActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onClick(id: Int?) {
        id?.let {
            val intent = Intent(this@MainActivity, NoteDetailActivity::class.java)
            intent.putExtra("selectedNote", it)
            startActivity(intent)
        }
    }


}