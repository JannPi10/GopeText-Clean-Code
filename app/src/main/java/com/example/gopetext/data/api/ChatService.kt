package com.example.gopetext.data.api

import com.example.gopetext.data.model.Contact
import com.example.gopetext.data.model.Message
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatService {

    @GET("api/chats")
    suspend fun getChats(): Response<ChatsResponse>

    @POST("api/chats")
    suspend fun createChat(@Body request: CreateChatRequest): Response<Contact> // crea chat individual

    @GET("api/chats/{id}/messages")
    suspend fun getMessages(@Path("id") chatId: Int): MessagesResponse

    @POST("api/chats/{chatId}/messages")
    suspend fun sendMessage(
        @Path("chatId") chatId: Int,
        @Body request: SendMessageRequest
    ): Response<Message>
}





