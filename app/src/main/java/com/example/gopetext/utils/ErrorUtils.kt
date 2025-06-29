package com.example.gopetext.utils

import com.example.gopetext.data.api.ErrorResponse
import com.google.gson.Gson

object ErrorUtils {
    fun parseError(json: String?): ErrorResponse? {
        return try {
            Gson().fromJson(json, ErrorResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }
}