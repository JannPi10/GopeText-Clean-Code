package com.example.gopetext.utils

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException

@RunWith(MockitoJUnitRunner::class)
class LoggerTest {

    @Mock
    private lateinit var mockLogDelegate: Logger.LogDelegate

    @Before
    fun setUp() {
        // Guardar el delegado original para restaurarlo después
        Logger.logDelegate = mockLogDelegate
    }

    @Test
    fun `debug log with custom tag and message calls delegate d with correct parameters`() {
        // Given
        val testTag = "TestTag"
        val testMessage = "Test debug message"

        // When
        Logger.d(testTag, testMessage)

        // Then
        verify(mockLogDelegate).d(testTag, testMessage)
    }

    @Test
    fun `debug log with default tag calls delegate d with default tag`() {
        // Given
        val testMessage = "Test debug message with default tag"
        val defaultTag = "GoPetApp"

        // When
        Logger.d(message = testMessage)

        // Then
        verify(mockLogDelegate).d(defaultTag, testMessage)
    }

    @Test
    fun `error log with custom tag and message calls delegate e with correct parameters`() {
        // Given
        val testTag = "TestTag"
        val testMessage = "Test error message"

        // When
        Logger.e(testTag, testMessage)

        // Then
        verify(mockLogDelegate).e(testTag, testMessage, null)
    }

    @Test
    fun `error log with throwable calls delegate e with throwable`() {
        // Given
        val testTag = "TestTag"
        val testMessage = "Test error with throwable"
        val testException = RuntimeException("Test exception")

        // When
        Logger.e(testTag, testMessage, testException)

        // Then
        verify(mockLogDelegate).e(testTag, testMessage, testException)
    }

    @Test
    fun `error log without throwable calls delegate e with null throwable`() {
        // Given
        val testTag = "TestTag"
        val testMessage = "Test error without throwable"

        // When
        Logger.e(testTag, testMessage, null)

        // Then
        verify(mockLogDelegate).e(testTag, testMessage, null)
    }
}
