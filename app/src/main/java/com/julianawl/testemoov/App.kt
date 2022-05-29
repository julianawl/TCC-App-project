package com.julianawl.testemoov

import android.app.Application
import com.julianawl.testemoov.graphics.model.SetList

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        ModelPreferencesManager.with(this)
        ModelPreferencesManager.put(SetList(null), "COMPOSITION")
    }
}