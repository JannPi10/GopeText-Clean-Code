package com.example.gopetext.data.api

import com.example.gopetext.data.model.Message
import com.google.gson.annotations.SerializedName

data class MessagesResponse(
    @SerializedName("messages")
    val messages: List<Message>
)
