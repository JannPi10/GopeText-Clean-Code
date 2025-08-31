package com.example.gopetext.data.repository

import com.example.gopetext.core.ApiResult
import com.example.gopetext.core.safeApiCall
import com.example.gopetext.core.safeBodyCall
import com.example.gopetext.data.api.ChatService
import com.example.gopetext.data.api.CreateGroupRequest
import com.example.gopetext.data.api.CreateChatRequest
import com.example.gopetext.data.api.MessagesResponse
import com.example.gopetext.data.api.SendMessageRequest
import com.example.gopetext.data.model.Contact
import com.example.gopetext.data.model.Message

interface ChatRepository {
    suspend fun fetchMessages(chatId: Int): ApiResult<List<Message>>
    suspend fun sendMessage(chatId: Int, request: SendMessageRequest): ApiResult<Unit>
    suspend fun leaveGroup(chatId: Int): ApiResult<Unit>
    suspend fun createChatWith(userId: Int): ApiResult<Int>
}

class RemoteChatRepository(private val api: ChatService) : ChatRepository {

    override suspend fun fetchMessages(chatId: Int): ApiResult<List<Message>> {
        val result: ApiResult<MessagesResponse> = safeBodyCall { api.getMessages(chatId) }
        return when (result) {
            is ApiResult.Success -> ApiResult.Success(result.data.messages)
            is ApiResult.HttpError -> result
            is ApiResult.NetworkError -> result
        }
    }

    override suspend fun sendMessage(chatId: Int, request: SendMessageRequest): ApiResult<Unit> {
        val result: ApiResult<Message> = safeApiCall { api.sendMessage(chatId, request) }
        return when (result) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.HttpError -> result
            is ApiResult.NetworkError -> result
        }
    }

    override suspend fun leaveGroup(chatId: Int): ApiResult<Unit> {
        val result: ApiResult<Unit> = safeApiCall { api.leaveGroup(chatId, CreateGroupRequest(name = "none", members = emptyList())) }
        return when (result) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.HttpError -> result
            is ApiResult.NetworkError -> result
        }
    }

    override suspend fun createChatWith(userId: Int): ApiResult<Int> {
        val result: ApiResult<Contact> = safeApiCall { api.createChat(CreateChatRequest(userId)) }
        return when (result) {
            is ApiResult.Success -> ApiResult.Success(result.data.id)
            is ApiResult.HttpError -> result
            is ApiResult.NetworkError -> result
        }
    }
}
