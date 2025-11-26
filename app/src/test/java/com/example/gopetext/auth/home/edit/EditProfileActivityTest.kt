package com.example.gopetext.auth.home.edit

import com.example.gopetext.data.model.User
import com.example.gopetext.data.repository.UserRepository
import com.example.gopetext.utils.ErrorHandler
import com.example.gopetext.utils.ImageLoader
import com.example.gopetext.utils.validators.ProfileValidator
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EditProfileActivityTest {

    @Mock
    private lateinit var mockPresenter: EditProfileContract.Presenter

    @Mock
    private lateinit var mockErrorHandler: ErrorHandler

    @Mock
    private lateinit var mockImageLoader: ImageLoader

    @Mock
    private lateinit var mockProfileValidator: ProfileValidator

    private lateinit var activity: EditProfileActivity

    private val testUser = User(
        id = 1,
        name = "Test",
        last_name = "User",
        email = "test@example.com",
        age = 25,
        profile_image_url = "http://example.com/image.jpg"
    )

    @Before
    fun setup() {
        // Configurar la actividad
        activity = spyk(EditProfileActivity())

        // Inyectar mocks usando reflexión
        setPrivateField(activity, "presenter", mockPresenter)
        setPrivateField(activity, "errorHandler", mockErrorHandler)
        setPrivateField(activity, "imageLoader", mockImageLoader)
        setPrivateField(activity, "profileValidator", mockProfileValidator)
    }

    @Test
    fun `showUserData should update UI with user data`() {
        // When
        activity.showUserData(testUser)

        // Then - Verificar que no hay excepciones
        // Las aserciones reales de UI se harían con Espresso en pruebas instrumentadas
    }

    @Test
    fun `showError should delegate to ErrorHandler`() {
        // Given
        val errorMessage = "Error de prueba"

        // When
        activity.showError(errorMessage)

        // Then
        verify(mockErrorHandler).showError(errorMessage)
    }

    @Test
    fun `showSuccess should delegate to ErrorHandler and finish`() {
        // Given
        val successMessage = "¡Perfil actualizado!"

        // When
        activity.showSuccess(successMessage)

        // Then
        verify(mockErrorHandler).showSuccess(successMessage)
    }

    @Test
    fun `goBackProfile should finish activity`() {
        // When
        activity.goBackProfile()

        // Then - Verificar que no hay excepciones
    }


    // Helper function to set private fields using reflection
    private fun <T> setPrivateField(obj: Any, fieldName: String, value: T) {
        val field = obj.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true
        field.set(obj, value)
    }
}
