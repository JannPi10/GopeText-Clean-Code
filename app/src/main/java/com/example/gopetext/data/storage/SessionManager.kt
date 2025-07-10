package com.example.gopetext.data.storage

import android.content.Context
import android.content.SharedPreferences
import kotlin.coroutines.CoroutineContext

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "gopetext_prefs"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_USER_ID = "user_id" // 👈 Agregado
    }

    fun saveAccessToken(token: String) {
        prefs.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    fun saveUserId(userId: Int) { // 👈 Agregado
        prefs.edit().putInt(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): Int { // 👈 Agregado
        return prefs.getInt(KEY_USER_ID, -1)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

}

