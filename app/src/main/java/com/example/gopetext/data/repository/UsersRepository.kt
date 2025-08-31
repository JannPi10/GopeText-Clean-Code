package com.example.gopetext.data.repository

import com.example.gopetext.core.ApiResult
import com.example.gopetext.core.safeApiCall
import com.example.gopetext.data.api.AuthService
import com.example.gopetext.data.model.UserChat

interface UsersRepository {
    suspend fun getAllUsersForChat(): ApiResult<List<UserChat>>
}

class RemoteUsersRepository(private val api: AuthService) : UsersRepository {
    override suspend fun getAllUsersForChat(): ApiResult<List<UserChat>> {
        return when (val result = safeApiCall { api.getAllUsers() }) {
            is ApiResult.Success -> {
                val chatUsers = result.data.users.map {
                    UserChat(id = it.id, name = it.name, profile_image_url = it.profile_image_url)
                }
                ApiResult.Success(chatUsers)
            }
            is ApiResult.HttpError -> result
            is ApiResult.NetworkError -> result
        }
    }
}
