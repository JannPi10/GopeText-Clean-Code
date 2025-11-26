package com.example.gopetext.data.repository

import com.example.gopetext.core.ApiResult
import com.example.gopetext.data.api.AuthService
import com.example.gopetext.data.api.LoginRequest
import com.example.gopetext.data.api.LoginResponse
import com.example.gopetext.data.api.UserResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteAuthRepositoryTest {

    private lateinit var repository: RemoteAuthRepository
    private val authService: AuthService = mockk()

    @Before
    fun setUp() {
        repository = RemoteAuthRepository(authService)
    }

    @Test
    fun `login should return Success with LoginResponse when API call is successful`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val expectedResponse = LoginResponse(
            token = "test_token",
            user = UserResponse(id = 1, email = email, name = "Test User")
        )
        
        coEvery { 
            authService.login(LoginRequest(email, password)) 
        } returns Response.success(expectedResponse)

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue(result is ApiResult.Success)
        assertEquals(expectedResponse, (result as ApiResult.Success).data)
        coVerify { authService.login(LoginRequest(email, password)) }
    }

    @Test
    fun `login should return HttpError when API returns error response`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "wrongpassword"
        val errorCode = 401
        val errorBody = "Unauthorized".toResponseBody("text/plain".toMediaTypeOrNull())
        
        coEvery { 
            authService.login(LoginRequest(email, password)) 
        } returns Response.error(errorCode, errorBody)

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue(result is ApiResult.HttpError)
        assertEquals(errorCode, (result as ApiResult.HttpError).code)
        coVerify { authService.login(LoginRequest(email, password)) }
    }

    @Test
    fun `login should return NetworkError when network exception occurs`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val exception = HttpException(
            Response.error<LoginResponse>(
                500,
                "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        )
        
        coEvery { 
            authService.login(LoginRequest(email, password)) 
        } throws exception

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue(result is ApiResult.NetworkError)
        assertEquals(exception.message, (result as ApiResult.NetworkError).message)
        coVerify { authService.login(LoginRequest(email, password)) }
    }

    @Test
    fun `login should handle unexpected exceptions`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val exception = RuntimeException("Unexpected error")
        
        coEvery { 
            authService.login(LoginRequest(email, password)) 
        } throws exception

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue(result is ApiResult.NetworkError)
        assertEquals(exception.message, (result as ApiResult.NetworkError).message)
        coVerify { authService.login(LoginRequest(email, password)) }
    }
}
