package com.hasanakcay.todoo.util

import android.content.Context
import com.hasanakcay.todoo.model.Note
import io.realm.RealmResults

interface TodooCrud {

    fun getAllNote(context: Context): RealmResults<Note>
    fun saveNote(context: Context, note: Note)
    fun deleteNote(context: Context, id: Int)
    fun findNote(context: Context, id: Int): Note?
    fun getId(context: Context): Number?
}