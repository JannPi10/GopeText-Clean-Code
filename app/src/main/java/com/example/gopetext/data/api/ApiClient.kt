// app/src/main/java/com/example/gopetext/data/api/ApiClient.kt
package com.example.gopetext.data.api

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.gopetext.data.storage.SessionManager
import com.example.gopetext.utils.Constants

object ApiClient {

    private lateinit var retrofit: Retrofit

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
    }

    fun getService(): AuthService {
        check(::retrofit.isInitialized) { "ApiClient not initialized. Call ApiClient.init(context) first." }
        return retrofit.create(AuthService::class.java)
    }

    fun <T> createService(service: Class<T>): T {
        check(::retrofit.isInitialized) { "ApiClient not initialized. Call ApiClient.init(context) first." }
        return retrofit.create(service)
    }

    inline fun <reified T> createService(): T = createService(T::class.java)
}
