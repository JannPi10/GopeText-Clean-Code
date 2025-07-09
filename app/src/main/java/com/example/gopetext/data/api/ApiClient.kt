package com.example.gopetext.data.api

import android.content.Context
import android.util.Log
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
            .addInterceptor(AuthInterceptor(sessionManager))
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        Log.d("ApiClient", "Retrofit inicializado con baseUrl: ${Constants.BASE_URL}")
    }

    fun getService(): AuthService {
        if (!::retrofit.isInitialized) {
            Log.e("ApiClient", "Error: ApiClient no inicializado antes de usar getService()")
            throw IllegalStateException("ApiClient no ha sido inicializado.")
        }
        return retrofit.create(AuthService::class.java)
    }
}






