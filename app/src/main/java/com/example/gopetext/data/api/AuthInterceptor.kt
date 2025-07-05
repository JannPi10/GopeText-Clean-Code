package com.example.gopetext.data.api

import android.util.Log
import com.example.gopetext.data.storage.SessionManager
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionManager.getAccessToken()
        Log.d("AuthInterceptor", "Token enviado: Bearer $token")

        val requestBuilder = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")

        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}


/** Para actualizar el proyecto
 * git fetch origin
 * git checkout dev
 * git pull origin dev
 */
