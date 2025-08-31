package com.example.gopetext.data.repository

import com.example.gopetext.core.ApiResult
import com.example.gopetext.core.safeApiCall
import com.example.gopetext.data.api.AuthService
import com.example.gopetext.data.api.LoginRequest
import com.example.gopetext.data.api.LoginResponse

interface AuthRepository {
    suspend fun login(email: String, password: String): ApiResult<LoginResponse>
}

class RemoteAuthRepository(private val api: AuthService) : AuthRepository {
    override suspend fun login(email: String, password: String): ApiResult<LoginResponse> =
        safeApiCall { api.login(LoginRequest(email, password)) }
}
