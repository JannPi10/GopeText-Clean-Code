package com.example.gopetext.data.model

data class User(
    val id: Int,
    val name: String,
    val apellido: String,
    val edad: Int,
    val email: String,
    val password: String,
    val confirmPassword: String
)
