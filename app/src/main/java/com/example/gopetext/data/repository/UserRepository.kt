package com.example.gopetext.data.repository

import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.api.AuthService
import com.example.gopetext.data.api.UpdateUserRequest
import com.example.gopetext.data.api.UserSingleResponse
import com.example.gopetext.data.storage.SessionManager
import com.example.gopetext.utils.Result
import okhttp3.MultipartBody

class UserRepository(private val sessionManager: SessionManager) {

    private val authService: AuthService = ApiClient.getService()

    suspend fun getUserProfile(): Result<UserSingleResponse> {
        return try {
            val response = authService.getUserProfile()
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("No se pudo cargar el perfil.")
            }
        } catch (e: Exception) {
            Result.Error("Error de red al cargar perfil.")
        }
    }

    suspend fun updateUserProfile(
        name: String,
        lastName: String,
        age: Int,
        photo: MultipartBody.Part?
    ): Result<Unit> {
        return try {
            val request = UpdateUserRequest(name, lastName, age)
            val updateResponse = authService.updateUserProfile(request)

            if (!updateResponse.isSuccessful) {
                return Result.Error("Error al actualizar datos.")
            }

            if (photo != null) {
                val photoResponse = authService.uploadProfileImage(photo)
                if (!photoResponse.isSuccessful) {
                    return Result.Error("Error al subir imagen.")
                }
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error al actualizar: ${e.message}")
        }
    }
}