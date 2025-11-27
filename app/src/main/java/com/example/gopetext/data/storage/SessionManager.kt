package com.example.gopetext.data.storage

import android.content.Context
import android.util.Log
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "gopetext_prefs"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_USER_ID = "user_id"
    }



    fun saveAccessToken(token: String) {
        prefs.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    fun saveUserId(userId: Int) {
        Log.d("SessionManager", "Guardando userId: $userId")
        prefs.edit().putInt(KEY_USER_ID, userId).apply()
        // Verificar que se guardó correctamente
        val savedId = prefs.getInt(KEY_USER_ID, -1)
        Log.d("SessionManager", "userId guardado: $savedId")
    }

    fun getUserId(): Int {
        val userId = prefs.getInt(KEY_USER_ID, -1)
        Log.d("SessionManager", "Recuperando userId: $userId")
        return userId
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }
}


