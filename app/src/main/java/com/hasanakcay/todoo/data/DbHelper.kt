package com.hasanakcay.todoo.data

import com.hasanakcay.todoo.data.db.Note
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.coroutines.flow.Flow

interface DbHelper {

    fun getAllNote(realm: Realm): RealmResults<Note>
    fun saveNote(realm: Realm, note: Note)
    fun deleteNote(realm: Realm, id: Int)
    fun findNote(realm: Realm, id: Int): Note?
    fun getId(realm: Realm): Number?
    //fun searchNotes(context: Context, searching : String) : Flow<List<Note>>?
}