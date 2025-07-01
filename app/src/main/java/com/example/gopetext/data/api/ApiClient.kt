package com.example.gopetext.data.api

import android.content.Context
import com.example.gopetext.data.storage.SessionManager
import com.example.gopetext.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    lateinit var retrofit: Retrofit

    fun init(context: Context) {
        val sessionManager = SessionManager(context)

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager)) // ✅ ahora sí
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}




