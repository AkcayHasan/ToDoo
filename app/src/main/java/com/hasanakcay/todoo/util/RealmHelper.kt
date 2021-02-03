package com.hasanakcay.todoo.util

import android.content.Context
import com.hasanakcay.todoo.model.Note
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where

class RealmHelper : TodooCrud {

    override fun getAllNote(context: Context): RealmResults<Note> {
        Realm.init(context)
        val realm = Realm.getDefaultInstance()
        return realm.where<Note>().equalTo("description", "task").findAll()
    }

    override fun saveNote(context: Context, note: Note) {
        Realm.init(context)
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(note)
        realm.commitTransaction()
    }

    override fun deleteNote(context: Context, id: Int) {
        Realm.init(context)
        val realm = Realm.getDefaultInstance()
        val result = realm.where<Note>().equalTo("id", id).findFirst()
        realm.beginTransaction()
        result?.deleteFromRealm()
        realm.commitTransaction()
    }

    override fun findNote(context: Context, id: Int): Note? {
        Realm.init(context)
        val realm = Realm.getDefaultInstance()
        return realm.where<Note>().equalTo("id", id).findFirst()
    }

    override fun getId(context: Context): Number? {
        Realm.init(context)
        val realm = Realm.getDefaultInstance()
        return realm.where<Note>().max("id")
    }


}