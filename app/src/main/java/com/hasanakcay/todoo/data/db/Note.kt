package com.hasanakcay.todoo.data.db

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class Note(
    @Required
    @PrimaryKey
    var id: Int? = null,
    @Required
    var categoriesList: RealmList<String> = RealmList(),
    var header: String? = null,
    var date: String? = null,
    var note: String? = null,
    var priorityId: String? = null,
    var description: String? = null

) : RealmObject()
