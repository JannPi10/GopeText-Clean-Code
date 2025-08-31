package com.example.gopetext.data.repository

import com.example.gopetext.core.ApiResult
import com.example.gopetext.core.safeApiCall
import com.example.gopetext.data.api.AuthService
import com.example.gopetext.data.api.LogoutResponse

interface AccountRepository {
    suspend fun logout(): ApiResult<LogoutResponse>
}

class RemoteAccountRepository(private val api: AuthService) : AccountRepository {
    override suspend fun logout(): ApiResult<LogoutResponse> =
        safeApiCall { api.logout() }
}
