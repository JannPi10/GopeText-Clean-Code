package com.example.gopetext.data.repository

import com.example.gopetext.core.ApiResult
import com.example.gopetext.data.api.AuthService
import com.example.gopetext.data.api.CreateGroupRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface GroupRepository {
    suspend fun createGroup(name: String, members: List<Int>): ApiResult<Unit>
}

class RemoteGroupRepository(private val api: AuthService) : GroupRepository {
    override suspend fun createGroup(name: String, members: List<Int>): ApiResult<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val response = api.createGroup(CreateGroupRequest(name, members))
                when {
                    response.isSuccessful -> ApiResult.Success(Unit)
                    else -> ApiResult.HttpError(response.code(), response.message())
                }
            }.getOrElse {
                ApiResult.NetworkError(it.message ?: "Network error")
            }
        }
    }
}