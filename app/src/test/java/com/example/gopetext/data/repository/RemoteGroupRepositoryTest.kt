package com.example.gopetext.data.repository

import com.example.gopetext.core.ApiResult
import com.example.gopetext.data.api.AuthService
import com.example.gopetext.data.api.CreateGroupRequest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RemoteGroupRepositoryTest {

    private lateinit var authService: AuthService
    private lateinit var repository: RemoteGroupRepository

    @Before
    fun setup() {
        authService = mock()
        repository = RemoteGroupRepository(authService)
    }

    @Test
    fun `createGroup should return Success when API call is successful`() {
        runBlocking {
            val groupName = "Equipo Kotlin"
            val members = listOf(1, 2, 3)
            val response: Response<Unit> = Response.success(Unit)

            whenever(authService.createGroup(any())).thenReturn(response)

            val result = repository.createGroup(groupName, members)

            assertIs<ApiResult.Success<Unit>>(result)
            verify(authService).createGroup(CreateGroupRequest(groupName, members))
        }
    }

    @Test
    fun `createGroup should return HttpError when API call fails with error code`() {
        runBlocking {
            val groupName = "Equipo Kotlin"
            val members = listOf(1, 2, 3)
            val response: Response<Unit> = Response.error(400, mock())

            whenever(authService.createGroup(any())).thenReturn(response)

            val result = repository.createGroup(groupName, members)

            assertIs<ApiResult.HttpError>(result)
            assertEquals(400, (result as ApiResult.HttpError).code)
            verify(authService).createGroup(CreateGroupRequest(groupName, members))
        }
    }

    @Test
    fun `createGroup should return HttpError with correct status code`() = runBlocking {
        val groupName = "Grupo Test"
        val members = listOf(1, 2)
        val response: Response<Unit> = Response.error(401, mock())

        whenever(authService.createGroup(any())).thenReturn(response)

        val result = repository.createGroup(groupName, members)

        assertIs<ApiResult.HttpError>(result)
        assertEquals(401, (result as ApiResult.HttpError).code)
    }

    @Test
    fun `createGroup should return HttpError with message on 403 Forbidden`() = runBlocking {
        val groupName = "Grupo Restringido"
        val members = listOf(1, 2)
        val response: Response<Unit> = Response.error(403, mock())

        whenever(authService.createGroup(any())).thenReturn(response)

        val result = repository.createGroup(groupName, members)

        assertIs<ApiResult.HttpError>(result)
        assertEquals(403, (result as ApiResult.HttpError).code)
    }

    @Test
    fun `createGroup should return NetworkError when exception is thrown`() {
        runBlocking {
            val groupName = "Equipo Kotlin"
            val members = listOf(1, 2, 3)
            val exception = RuntimeException("Connection timeout")

            whenever(authService.createGroup(any())).thenThrow(exception)

            val result = repository.createGroup(groupName, members)

            assertIs<ApiResult.NetworkError>(result)
            assertEquals("Connection timeout", (result as ApiResult.NetworkError).message)
        }
    }

    @Test
    fun `createGroup should return NetworkError with default message when exception has no message`() {
        runBlocking {
            val groupName = "Equipo Kotlin"
            val members = listOf(1, 2, 3)
            val exception = RuntimeException()

            whenever(authService.createGroup(any())).thenThrow(exception)

            val result = repository.createGroup(groupName, members)

            assertIs<ApiResult.NetworkError>(result)
            assertEquals("Network error", (result as ApiResult.NetworkError).message)
        }
    }

    @Test
    fun `createGroup should create request with correct parameters`() = runBlocking {
        val groupName = "Nuevo Grupo"
        val members = listOf(5, 10, 15)
        val response: Response<Unit> = Response.success(Unit)

        whenever(authService.createGroup(any())).thenReturn(response)

        repository.createGroup(groupName, members)

        val argumentCaptor = argumentCaptor<CreateGroupRequest>()
        verify(authService).createGroup(argumentCaptor.capture())

        val capturedRequest = argumentCaptor.firstValue
        assertEquals(groupName, capturedRequest.name)
        assertEquals(members, capturedRequest.members)
    }

    @Test
    fun `createGroup should return HttpError on 500 Internal Server Error`() = runBlocking {
        val groupName = "Grupo Test"
        val members = listOf(1, 2)
        val response: Response<Unit> = Response.error(500, mock())

        whenever(authService.createGroup(any())).thenReturn(response)

        val result = repository.createGroup(groupName, members)

        assertIs<ApiResult.HttpError>(result)
        assertEquals(500, (result as ApiResult.HttpError).code)
    }

    @Test
    fun `createGroup should handle empty members list`() {
        runBlocking {
            val groupName = "Grupo Vacío"
            val members = emptyList<Int>()
            val response: Response<Unit> = Response.success(Unit)

            whenever(authService.createGroup(any())).thenReturn(response)

            val result = repository.createGroup(groupName, members)

            assertIs<ApiResult.Success<Unit>>(result)
            verify(authService).createGroup(CreateGroupRequest(groupName, members))
        }
    }

    @Test
    fun `createGroup should handle large members list`() {
        runBlocking {
            val groupName = "Grupo Grande"
            val members = (1..100).toList()
            val response: Response<Unit> = Response.success(Unit)

            whenever(authService.createGroup(any())).thenReturn(response)

            val result = repository.createGroup(groupName, members)

            assertIs<ApiResult.Success<Unit>>(result)
            verify(authService).createGroup(CreateGroupRequest(groupName, members))
        }
    }
}