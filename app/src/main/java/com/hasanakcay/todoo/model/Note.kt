package com.hasanakcay.todoo.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.PrimaryKey
import java.util.ArrayList

open class Note(

    @PrimaryKey
    var id: Int? = null,

    var header: String? = null,
    var date: String? = null,
    var note: String? = null,
    var categoriesList: RealmList<String> ?= null,
    var priorityId: String? = null,

    var description: String? = null

) : RealmObject()
