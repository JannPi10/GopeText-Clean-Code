package com.example.gopetext.data.repository

import com.example.gopetext.core.ApiResult
import com.example.gopetext.core.safeApiCall
import android.util.Log
import com.example.gopetext.data.api.AuthService
import com.example.gopetext.data.api.LoginRequest
import com.example.gopetext.data.api.LoginResponse

interface AuthRepository {
    suspend fun login(email: String, password: String): ApiResult<LoginResponse>
}

class RemoteAuthRepository(private val api: AuthService) : AuthRepository {
    override suspend fun login(email: String, password: String): ApiResult<LoginResponse> {
        Log.d("RemoteAuthRepository", "Iniciando login para: $email")
        return safeApiCall { 
            val response = api.login(LoginRequest(email, password))
            Log.d("RemoteAuthRepository", "Respuesta cruda del servidor: ${response.raw()}")
            Log.d("RemoteAuthRepository", "Código de respuesta: ${response.code()}")
            Log.d("RemoteAuthRepository", "Cuerpo de respuesta: ${response.body()}")
            response
        }.also { result ->
            when (result) {
                is ApiResult.Success -> {
                    Log.d("RemoteAuthRepository", "Login exitoso. User ID: ${result.data.user.id}")
                }
                is ApiResult.HttpError -> {
                    Log.e("RemoteAuthRepository", "Error HTTP ${result.code}: ${result.message}")
                }
                is ApiResult.NetworkError -> {
                    Log.e("RemoteAuthRepository", "Error de red: ${result.message}")
                }
            }
        }
    }
}
