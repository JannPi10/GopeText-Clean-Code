package com.example.gopetext.data.api

import android.app.Application
import com.example.gopetext.data.api.ApiClient

class GopeTextApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ApiClient.init(this)
    }
}