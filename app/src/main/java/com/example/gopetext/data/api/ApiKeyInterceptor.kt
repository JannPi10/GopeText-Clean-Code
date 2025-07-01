package com.example.gopetext.data.api

import android.util.Log
import com.example.gopetext.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("ApiKeyInterceptor", "Agregando API key al header")
        val request = chain.request().newBuilder()
            .addHeader("x-api-key", Constants.API_KEY)
            .addHeader("Content-Type", "application/json")
//            .addHeader("x-mock-match-request-body", "true")

            .build()
        return chain.proceed(request)
    }
}