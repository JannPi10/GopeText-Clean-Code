package com.example.gopetext.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.gopetext.utils.Constants

object RetrofitClient {
    private const val BASE_URL = Constants.BASE_URL  // usa constante si ya la tienes definida

    // Interceptor de logging para ver request y response en Logcat
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(ApiKeyInterceptor())  // tu interceptor con headers
        .addInterceptor(logging)              // logging para depuración
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)  // asegúrate que termina con "/"
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }
}
