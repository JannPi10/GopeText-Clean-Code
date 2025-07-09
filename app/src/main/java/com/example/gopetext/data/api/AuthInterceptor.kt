package com.example.gopetext.data.api

import android.util.Log
import com.example.gopetext.data.storage.SessionManager
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val token = sessionManager.getAccessToken()
        val request = chain.request()

        val isMultipart = request.body?.contentType()?.subtype == "form-data"

        val requestBuilder = request.newBuilder()

        if (!isMultipart) {
            requestBuilder.addHeader("Content-Type", "application/json")
        }

        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}



