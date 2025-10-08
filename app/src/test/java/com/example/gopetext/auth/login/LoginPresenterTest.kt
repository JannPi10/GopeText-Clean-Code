package com.example.gopetext.auth.login

import com.example.gopetext.data.repository.AuthRepository
import com.example.gopetext.data.storage.SessionManager
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class LoginPresenterTest {

    private lateinit var presenter: LoginPresenter
    private lateinit var view: LoginContract.View
    private lateinit var repository: AuthRepository
    private lateinit var sessionManager: SessionManager

    @Before
    fun setup() {
        view = mock()
        repository = mock()
        sessionManager = mock()
        presenter = LoginPresenter(view, repository, sessionManager)
    }

    @Test
    fun `checkSession with valid token should navigate to home`() {
        whenever(sessionManager.getAccessToken()).thenReturn("valid_token")
        presenter.checkSession()
        verify(view).navigateToHome()
    }

    @Test
    fun `checkSession with null token should not navigate`() {
        whenever(sessionManager.getAccessToken()).thenReturn(null)
        presenter.checkSession()
        verify(view, never()).navigateToHome()
    }

    @Test
    fun `checkSession with empty token should not navigate`() {
        whenever(sessionManager.getAccessToken()).thenReturn("")
        presenter.checkSession()
        verify(view, never()).navigateToHome()
    }

    @Test
    fun `checkSession with blank token should not navigate`() {
        whenever(sessionManager.getAccessToken()).thenReturn("   ")
        presenter.checkSession()
        verify(view, never()).navigateToHome()
    }

    @Test
    fun `presenter should be created with correct dependencies`() {
        assert(presenter != null)
    }

    @Test
    fun `sessionManager should be called when checking session`() {
        whenever(sessionManager.getAccessToken()).thenReturn("token")
        presenter.checkSession()
        verify(sessionManager).getAccessToken()
    }

    @Test
    fun `view should not be called when token is null`() {
        whenever(sessionManager.getAccessToken()).thenReturn(null)
        presenter.checkSession()
        verify(view, never()).navigateToHome()
        verify(view, never()).showLoginSuccess()
        verify(view, never()).showLoginError(any())
    }

    @Test
    fun `view should not be called when token is empty string`() {
        whenever(sessionManager.getAccessToken()).thenReturn("")
        presenter.checkSession()
        verify(view, never()).navigateToHome()
        verify(view, never()).showLoginSuccess()
        verify(view, never()).showLoginError(any())
    }

    @Test
    fun `checkSession should handle whitespace-only token`() {
        whenever(sessionManager.getAccessToken()).thenReturn("    ")
        presenter.checkSession()
        verify(view, never()).navigateToHome()
    }
}
