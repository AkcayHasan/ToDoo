package com.hasanakcay.todoo.data

import com.hasanakcay.todoo.data.db.Note
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where

class RealmDbHelper : DbHelper {

    override fun getAllNote(realm: Realm): RealmResults<Note> {
        return realm.where<Note>().equalTo("description", "task").findAll()
    }

    override fun saveNote(realm: Realm, note: Note) {
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(note)
        realm.commitTransaction()
    }

    override fun deleteNote(realm: Realm, id: Int) {
        val result = realm.where<Note>().equalTo("id", id).findFirst()
        realm.beginTransaction()
        result?.deleteFromRealm()
        realm.commitTransaction()
    }

    override fun findNote(realm: Realm, id: Int): Note? {
        return realm.where<Note>().equalTo("id", id).findFirst()
    }

    override fun getId(realm: Realm): Number? {
        return realm.where<Note>().max("id")
    }


}