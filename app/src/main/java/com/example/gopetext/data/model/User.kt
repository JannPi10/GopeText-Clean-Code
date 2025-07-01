package com.example.gopetext.data.model

data class User(
    val id: Int,
    val name: String,
    val last_name: String,
    val age: Int,
    val email: String,
    val password: String,
    val confirmPassword: String
)
