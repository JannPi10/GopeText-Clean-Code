package com.example.gopetext.core

import retrofit2.Response
import retrofit2.HttpException

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

suspend fun <T> safeBodyCall(call: suspend () -> T): ApiResult<T> =
    runCatching { call() }.fold(
        onSuccess = { ApiResult.Success(it) },
        onFailure = { t ->
            if (t is HttpException) {
                val msg = t.response()?.errorBody()?.string().orEmpty().ifBlank { t.message() ?: "HTTP error" }
                ApiResult.HttpError(t.code(), msg)
            } else {
                ApiResult.NetworkError(t.localizedMessage ?: "Unexpected error")
            }
        }
    )
