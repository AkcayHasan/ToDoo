package com.hasanakcay.todoo.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hasanakcay.todoo.R
import com.hasanakcay.todoo.adapter.NoteListAdapter
import com.hasanakcay.todoo.data.RealmDbHelper
import com.hasanakcay.todoo.data.db.Note
import com.hasanakcay.todoo.databinding.FragmentNotesBinding
import com.hasanakcay.todoo.ui.base.BaseFragment
import com.hasanakcay.todoo.util.AdapterClickListener
import com.hasanakcay.todoo.util.FragmentListener
import io.realm.Realm

class NotesFragment : BaseFragment<FragmentNotesBinding>(), AdapterClickListener {

    lateinit var realm : Realm
    lateinit var fragmentListener: FragmentListener

    override fun getViewBinding(): FragmentNotesBinding {
        return FragmentNotesBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Realm.init(binding.root.context)
        realm = Realm.getDefaultInstance()

        fragmentListener = activity as FragmentListener
        fragmentListener.whichFragment(R.id.notesFragment)

        actions()

        if (getAllNote().size == 0) {
            binding.emptyListTv.visibility = View.VISIBLE
            binding.rvNoteList.visibility = View.GONE
        } else {
            binding.emptyListTv.visibility = View.GONE
            binding.rvNoteList.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(binding.root.context)
                adapter = NoteListAdapter(getAllNote(), this@NotesFragment)
            }
        }
    }

    private fun getAllNote(): MutableList<Note> {
        return RealmDbHelper().getAllNote(realm)
    }

    private fun actions() {
        binding.floatingActionButton.setOnClickListener {
            val action = NotesFragmentDirections.notesFragmentToNoteDetailFragment()
            it.findNavController().navigate(action)
        }
    }

    override fun onNoteClick(id: Int?) {
        id?.let {
            val action = NotesFragmentDirections.notesFragmentToNoteDetailFragment(it)
            binding.root.findNavController().navigate(action)
        }
    }

    override fun onStop() {
        super.onStop()
        realm.close()
    }


}