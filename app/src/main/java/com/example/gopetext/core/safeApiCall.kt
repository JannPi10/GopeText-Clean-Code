package com.example.gopetext.core

import retrofit2.Response

suspend fun <T> safeApiCall(call: suspend () -> Response<T>): ApiResult<T> =
    runCatching { call() }.fold(
        onSuccess = { response ->
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) ApiResult.Success(body)
                else ApiResult.HttpError(response.code(), "Empty response body")
            } else {
                val msg = response.errorBody()?.string().orEmpty().ifBlank { response.message() }
                ApiResult.HttpError(response.code(), msg)
            }
        },
        onFailure = { ApiResult.NetworkError(it.localizedMessage ?: "Unexpected error") }
    )
