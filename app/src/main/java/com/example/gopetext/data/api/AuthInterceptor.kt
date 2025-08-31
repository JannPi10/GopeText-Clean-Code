package com.example.gopetext.data.api

import com.example.gopetext.data.storage.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionManager.getAccessToken()
        val original = chain.request()
        val isMultipart = original.body?.contentType()?.subtype == "form-data"

        val builder = original.newBuilder()
        if (!isMultipart && original.header("Content-Type").isNullOrEmpty()) {
            builder.addHeader("Content-Type", "application/json")
        }
        if (!token.isNullOrBlank()) {
            builder.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(builder.build())
    }
}
