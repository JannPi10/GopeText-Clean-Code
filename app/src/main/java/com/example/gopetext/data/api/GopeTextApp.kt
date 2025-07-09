package com.example.gopetext.data.api

import android.app.Application
import android.util.Log
import com.example.gopetext.data.api.ApiClient

class GopeTextApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("GopeTextApp", "Inicializando ApiClient")
        ApiClient.init(this)
    }
}
