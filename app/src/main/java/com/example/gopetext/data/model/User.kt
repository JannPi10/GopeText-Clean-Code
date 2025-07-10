package com.example.gopetext.data.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val name: String,
    @SerializedName("last_name") val last_name: String,
    val email: String,
    val age: Int,
    @SerializedName("profile_image_url") val profile_image_url: String?
)

