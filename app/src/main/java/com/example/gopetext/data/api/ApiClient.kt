package com.example.gopetext.data.api

import com.example.gopetext.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())  // ← solo si lo necesitas
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)  // ← este debe terminar en "/"
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}
