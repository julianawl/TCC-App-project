package com.julianawl.testemoov

import android.app.Application

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        ModelPreferencesManager.with(this)
    }
}