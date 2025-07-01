package com.example.gopetext.data.api

import com.example.gopetext.data.storage.SessionManager
import com.example.gopetext.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionManager.getAccessToken()

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
