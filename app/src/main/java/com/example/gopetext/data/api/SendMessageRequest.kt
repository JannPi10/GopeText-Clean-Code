package com.example.gopetext.data.api

import com.google.gson.annotations.SerializedName

data class SendMessageRequest(
    @SerializedName("message")
    val message: String
)