package com.hasanakcay.todoo.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class Note(

    @PrimaryKey
    var id: Int? = null,

    var header: String? = null,
    var date: String? = null,
    var note: String? = null,

    @Required
    var categoriesList: RealmList<String> = RealmList(),

    var priorityId: String? = null,
    var description: String? = null

) : RealmObject()
