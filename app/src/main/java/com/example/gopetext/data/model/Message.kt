package com.example.gopetext.data.model

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("id")
    val id: Int,

    @SerializedName("content")
    val content: String,

    @SerializedName("timestamp")
    val timestamp: String?,

    @SerializedName("sender")
    val sender: Int
)




