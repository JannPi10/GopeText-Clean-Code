package com.example.gopetext.data.api

import com.example.gopetext.data.model.Message
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatService {
    @GET("chat/messages")
    fun getMessages(): Call<List<Message>>

    @POST("chat/messages")
    fun sendMessage(@Body message: SendMessageRequest): Call<Message>
}
