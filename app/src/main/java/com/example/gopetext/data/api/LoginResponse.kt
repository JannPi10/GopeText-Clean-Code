package com.example.gopetext.data.api

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val token: String,
    val user: UserResponse
)

data class UserResponse(
    val id: Int,
    val email: String,
    val name: String
)
