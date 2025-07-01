package com.example.gopetext.data.storage

import android.content.Context
import android.content.SharedPreferences
import kotlin.coroutines.CoroutineContext

class SessionManager(context: Context) { // ✅ Context de Android

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "gopetext_prefs"
        private const val KEY_ACCESS_TOKEN = "access_token"
    }

    fun saveAccessToken(token: String) {
        prefs.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean {
        return getAccessToken() != null
    }
}
