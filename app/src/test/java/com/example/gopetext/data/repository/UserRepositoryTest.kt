package com.example.gopetext.data.repository

import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.api.AuthService
import com.example.gopetext.data.api.UpdateUserRequest
import com.example.gopetext.data.api.UserSingleResponse
import com.example.gopetext.data.model.User
import com.example.gopetext.data.storage.SessionManager
import com.example.gopetext.utils.Result
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {

    private lateinit var repository: UserRepository
    private val mockAuthService: AuthService = mockk()
    private val mockSessionManager: SessionManager = mockk(relaxed = true)

    @Before
    fun setUp() {
        // Mock the ApiClient to return our mock AuthService
        mockkObject(ApiClient)
        every { ApiClient.getService() } returns mockAuthService
        
        repository = UserRepository(mockSessionManager)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getUserProfile should return Success with user data when API call is successful`() = runTest {
        // Given
        val expectedUser = User(
            id = 1,
            name = "Test User",
            last_name = "Lastname",
            email = "test@example.com",
            age = 25,
            profile_image_url = "https://example.com/profile.jpg"
        )
        val response = Response.success(UserSingleResponse(expectedUser))
        coEvery { mockAuthService.getUserProfile() } returns response

        // When
        val result = repository.getUserProfile()

        // Then
        assertTrue(result is Result.Success)
        assertEquals(expectedUser, (result as Result.Success).data.user)
        coVerify { mockAuthService.getUserProfile() }
    }

    @Test
    fun `getUserProfile should return Error when API call fails`() = runTest {
        // Given
        coEvery { mockAuthService.getUserProfile() } returns Response.error(
            500,
            "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        // When
        val result = repository.getUserProfile()

        // Then
        assertTrue(result is Result.Error)
        assertEquals("No se pudo cargar el perfil.", (result as Result.Error).message)
    }

    @Test
    fun `getUserProfile should return Error when exception occurs`() = runTest {
        // Given
        coEvery { mockAuthService.getUserProfile() } throws Exception("Network error")

        // When
        val result = repository.getUserProfile()

        // Then
        assertTrue(result is Result.Error)
        assertEquals("Error de red al cargar perfil.", (result as Result.Error).message)
    }

    @Test
    fun `updateUserProfile should return Success when both updates are successful`() = runTest {
        // Given
        val name = "Updated Name"
        val lastName = "Updated Lastname"
        val age = 30
        val photo: MultipartBody.Part? = null

        coEvery { 
            mockAuthService.updateUserProfile(any()) 
        } returns Response.success(Unit)

        // When
        val result = repository.updateUserProfile(name, lastName, age, photo)

        // Then
        assertTrue(result is Result.Success)
        coVerify { 
            mockAuthService.updateUserProfile(
                match { it.name == name && it.last_name == lastName && it.age == age }
            ) 
        }
        coVerify(exactly = 0) { mockAuthService.uploadProfileImage(any()) }
    }

    @Test
    fun `updateUserProfile should upload photo when provided`() = runTest {
        // Given
        val photo = MultipartBody.Part.createFormData(
            "profile_image",
            "test.jpg",
            "test content".toRequestBody("image/jpeg".toMediaTypeOrNull())
        )
        
        coEvery { mockAuthService.updateUserProfile(any()) } returns Response.success(Unit)
        coEvery { 
            mockAuthService.uploadProfileImage(any()) 
        } returns Response.success(mockk())

        // When
        val result = repository.updateUserProfile("name", "lastname", 25, photo)

        // Then
        assertTrue(result is Result.Success)
        coVerify { mockAuthService.uploadProfileImage(photo) }
    }

    @Test
    fun `updateUserProfile should return Error when profile update fails`() = runTest {
        // Given
        coEvery { 
            mockAuthService.updateUserProfile(any()) 
        } returns Response.error(400, "Bad Request".toResponseBody(null))

        // When
        val result = repository.updateUserProfile("name", "lastname", 25, null)

        // Then
        assertTrue(result is Result.Error)
        assertEquals("Error al actualizar datos.", (result as Result.Error).message)
    }

    @Test
    fun `updateUserProfile should return Error when photo upload fails`() = runTest {
        // Given
        val photo = MultipartBody.Part.createFormData(
            "profile_image",
            "test.jpg",
            "test content".toRequestBody("image/jpeg".toMediaTypeOrNull())
        )
        
        coEvery { mockAuthService.updateUserProfile(any()) } returns Response.success(Unit)
        coEvery { 
            mockAuthService.uploadProfileImage(any()) 
        } returns Response.error(500, "Server Error".toResponseBody(null))

        // When
        val result = repository.updateUserProfile("name", "lastname", 25, photo)

        // Then
        assertTrue(result is Result.Error)
        assertEquals("Error al subir imagen.", (result as Result.Error).message)
    }

    @Test
    fun `updateUserProfile should handle exceptions`() = runTest {
        // Given
        coEvery { 
            mockAuthService.updateUserProfile(any()) 
        } throws Exception("Network error")

        // When
        val result = repository.updateUserProfile("name", "lastname", 25, null)

        // Then
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).message.startsWith("Error al actualizar:"))
    }
}
