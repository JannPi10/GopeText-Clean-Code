package com.example.gopetext.data.api

import com.example.gopetext.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("x-api-key", Constants.API_KEY)
            .addHeader("Content-Type", "application/json")
            .addHeader("x-mock-match-request-body", "false")
            .build()
        return chain.proceed(request)
    }
}
