package com.example.gopetext.data.storage

import android.content.Context
import android.content.SharedPreferences
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class SessionManagerTest {

    private lateinit var sessionManager: SessionManager
    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @Before
    fun setup() {
        context = mock()
        sharedPreferences = mock()
        editor = mock()
        
        whenever(context.getSharedPreferences("gopetext_prefs", Context.MODE_PRIVATE))
            .thenReturn(sharedPreferences)
        whenever(sharedPreferences.edit()).thenReturn(editor)
        whenever(editor.putString(any(), any())).thenReturn(editor)
        whenever(editor.putInt(any(), any())).thenReturn(editor)
        whenever(editor.clear()).thenReturn(editor)
        
        sessionManager = SessionManager(context)
    }

    @Test
    fun `saveAccessToken should save token to SharedPreferences`() {
        val token = "test_token_123"
        sessionManager.saveAccessToken(token)
        verify(editor).putString("access_token", token)
        verify(editor).apply()
    }

    @Test
    fun `getAccessToken should return token from SharedPreferences`() {
        val expectedToken = "stored_token"
        whenever(sharedPreferences.getString("access_token", null)).thenReturn(expectedToken)
        val result = sessionManager.getAccessToken()
        assert(result == expectedToken)
        verify(sharedPreferences).getString("access_token", null)
    }

    @Test
    fun `getAccessToken should return null when no token stored`() {
        whenever(sharedPreferences.getString("access_token", null)).thenReturn(null)
        val result = sessionManager.getAccessToken()
        assert(result == null)
    }

    @Test
    fun `saveUserId should save userId to SharedPreferences`() {
        val userId = 42
        sessionManager.saveUserId(userId)
        verify(editor).putInt("user_id", userId)
        verify(editor).apply()
    }

    @Test
    fun `getUserId should return userId from SharedPreferences`() {
        val expectedUserId = 123
        whenever(sharedPreferences.getInt("user_id", -1)).thenReturn(expectedUserId)
        val result = sessionManager.getUserId()
        assert(result == expectedUserId)
        verify(sharedPreferences).getInt("user_id", -1)
    }

    @Test
    fun `getUserId should return -1 when no userId stored`() {
        whenever(sharedPreferences.getInt("user_id", -1)).thenReturn(-1)
        val result = sessionManager.getUserId()
        assert(result == -1)
    }

    @Test
    fun `clearSession should clear all SharedPreferences`() {
        sessionManager.clearSession()
        verify(editor).clear()
        verify(editor).apply()
    }

    @Test
    fun `fetchAuthToken should return same as getAccessToken`() {
        val expectedToken = "auth_token"
        whenever(sharedPreferences.getString("access_token", null)).thenReturn(expectedToken)
        val result = sessionManager.fetchAuthToken()
        assert(result == expectedToken)
        verify(sharedPreferences).getString("access_token", null)
    }

    @Test
    fun `fetchAuthToken should return null when no token`() {
        whenever(sharedPreferences.getString("access_token", null)).thenReturn(null)
        val result = sessionManager.fetchAuthToken()
        assert(result == null)
    }

    @Test
    fun `saveAccessToken with empty string should save empty string`() {
        val emptyToken = ""
        sessionManager.saveAccessToken(emptyToken)
        verify(editor).putString("access_token", emptyToken)
        verify(editor).apply()
    }

    @Test
    fun `saveUserId with zero should save zero`() {
        val userId = 0
        sessionManager.saveUserId(userId)
        verify(editor).putInt("user_id", userId)
        verify(editor).apply()
    }

    @Test
    fun `saveUserId with negative number should save negative number`() {
        val userId = -5
        sessionManager.saveUserId(userId)
        verify(editor).putInt("user_id", userId)
        verify(editor).apply()
    }
}
