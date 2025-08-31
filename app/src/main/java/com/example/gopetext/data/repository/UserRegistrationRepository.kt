package com.example.gopetext.data.repository

import com.example.gopetext.core.ApiResult
import com.example.gopetext.core.safeApiCall
import com.example.gopetext.data.api.AuthService
import com.example.gopetext.data.api.RegisterRequest
import com.example.gopetext.data.api.RegisterResponse

interface UserRegistrationRepository {
    suspend fun registerUser(
        firstName: String,
        lastName: String,
        age: Int,
        email: String,
        password: String,
        confirmPassword: String
    ): ApiResult<RegisterResponse>
}

class RemoteUserRegistrationRepository(private val api: AuthService) : UserRegistrationRepository {
    override suspend fun registerUser(
        firstName: String,
        lastName: String,
        age: Int,
        email: String,
        password: String,
        confirmPassword: String
    ): ApiResult<RegisterResponse> =
        safeApiCall {
            api.register(
                RegisterRequest(
                    name = firstName,
                    last_name = lastName,
                    age = age,
                    email = email,
                    password = password,
                    confirm_password = confirmPassword
                )
            )
        }
}
