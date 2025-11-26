package com.example.gopetext.data.api

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class LoginResponseTest {

    @Test
    fun `LoginResponse should be correctly parsed from JSON`() {
        // Given
        val json = """
            {
                "token": "test_token_123",
                "user": {
                    "id": 1,
                    "email": "test@example.com",
                    "name": "Test User"
                }
            }
        """.trimIndent()

        // When
        val loginResponse = Gson().fromJson(json, LoginResponse::class.java)

        // Then
        assertEquals("test_token_123", loginResponse.token)
        assertEquals(1, loginResponse.user.id)
        assertEquals("test@example.com", loginResponse.user.email)
        assertEquals("Test User", loginResponse.user.name)
    }

    @Test
    fun `LoginResponse properties should match constructor parameters`() {
        // Given
        val user = UserResponse(
            id = 2,
            email = "another@example.com",
            name = "Another User"
        )
        
        // When
        val loginResponse = LoginResponse(
            token = "another_token_456",
            user = user
        )

        // Then
        assertEquals("another_token_456", loginResponse.token)
        assertEquals(2, loginResponse.user.id)
        assertEquals("another@example.com", loginResponse.user.email)
        assertEquals("Another User", loginResponse.user.name)
    }

    @Test
    fun `UserResponse properties should match constructor parameters`() {
        // When
        val user = UserResponse(
            id = 3,
            email = "user@test.com",
            name = "Test User"
        )

        // Then
        assertEquals(3, user.id)
        assertEquals("user@test.com", user.email)
        assertEquals("Test User", user.name)
    }

    @Test
    fun `LoginResponse equals and hashCode should work correctly`() {
        // Given
        val user1 = UserResponse(1, "test@example.com", "Test User")
        val user2 = UserResponse(1, "test@example.com", "Test User")
        val loginResponse1 = LoginResponse("token123", user1)
        val loginResponse2 = LoginResponse("token123", user2)

        // Then
        assertEquals(loginResponse1, loginResponse2)
        assertEquals(loginResponse1.hashCode(), loginResponse2.hashCode())
    }
}
