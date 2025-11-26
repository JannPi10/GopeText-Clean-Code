package com.example.gopetext.utils

import android.content.Context
import android.widget.Toast
import io.mockk.*
import org.junit.Before
import org.junit.Test

class ErrorHandlerTest {

    private lateinit var errorHandler: ErrorHandler
    private val mockContext: Context = mockk(relaxed = true)

    @Before
    fun setUp() {
        mockkStatic("android.widget.Toast")
        
        // Configurar el mock estático para Toast
        mockkStatic(Toast::class)
        
        // Configurar el comportamiento de makeText
        val mockToast: Toast = mockk(relaxed = true)
        every { 
            Toast.makeText(
                any(), 
                any<CharSequence>(), 
                any()
            ) 
        } returns mockToast
        
        errorHandler = ErrorHandler(mockContext)
    }

    @Test
    fun `showError should call Toast with correct message`() {
        // Given
        val errorMessage = "Error message"
        
        // When
        errorHandler.showError(errorMessage)
        
        // Then - Verificar que se llamó a makeText con el mensaje correcto
        verify {
            Toast.makeText(
                any(),
                errorMessage,
                any()
            )
        }
    }

    @Test
    fun `showSuccess should call Toast with correct message`() {
        // Given
        val successMessage = "Success message"
        
        // When
        errorHandler.showSuccess(successMessage)
        
        // Then - Verificar que se llamó a makeText con el mensaje correcto
        verify {
            Toast.makeText(
                any(),
                successMessage,
                any()
            )
        }
    }
    
    @Test
    fun `showError with empty message should still work`() {
        // When
        errorHandler.showError("")
        
        // Then - Verificar que se llamó a makeText
        verify { 
            Toast.makeText(
                any(),
                "",
                any()
            )
        }
    }
}
