package com.example.gopetext.data.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>


    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}