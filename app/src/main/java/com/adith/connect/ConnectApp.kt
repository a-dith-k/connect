package com.adith.connect

import android.app.Application
import com.google.firebase.FirebaseApp

class ConnectApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this) // ✅ Initialize Firebase context
    }
}
