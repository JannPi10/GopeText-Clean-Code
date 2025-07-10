package com.example.gopetext.data.api

import com.example.gopetext.data.model.User
import com.google.gson.annotations.SerializedName
import org.w3c.dom.Text

/**
data class LoginResponse(
    val access_token: String,
    val refresh_token: String,
    val token_type: String,
    val expires_in: Int
)**/
data class LoginResponse(
    val token: String,
    val user_id: Int
)
