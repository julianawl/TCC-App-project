package com.julianawl.testemoov

import android.app.Application
import com.julianawl.testemoov.data.ModelPreferencesManager

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        ModelPreferencesManager.with(this)
    }
}