package com.hasanakcay.todoo

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration

@HiltAndroidApp
class App : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val configuration = RealmConfiguration.Builder()
            .name("todoo.db")
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .build()

        Realm.setDefaultConfiguration(configuration)
    }
}