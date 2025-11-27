package com.example.gopetext.auth.login

import android.util.Log
import com.example.gopetext.core.ApiResult
import com.example.gopetext.data.repository.AuthRepository
import com.example.gopetext.data.storage.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginPresenter(
    private val view: LoginContract.View,
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) : LoginContract.Presenter {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun login(email: String, password: String) {
        scope.launch {
            when (val result = repository.login(email, password)) {
                is ApiResult.Success -> {
                    val body = result.data
                    val userId = body.user.id
                    Log.d("LoginPresenter", "Respuesta exitosa del servidor. user_id: $userId, token: ${body.token.take(10)}...")
                    
                    // Guardar token y user_id
                    sessionManager.saveAccessToken(body.token)
                    sessionManager.saveUserId(userId)
                    
                    // Verificar que se guardaron correctamente
                    val savedToken = sessionManager.getAccessToken()
                    val savedUserId = sessionManager.getUserId()
                    Log.d("LoginPresenter", "Datos guardados - Token: ${savedToken?.take(10)}..., UserId: $savedUserId")
                    
                    withContext(Dispatchers.Main) { 
                        Log.d("LoginPresenter", "Navegando a la pantalla principal...")
                        view.showLoginSuccess() 
                    }
                }
                is ApiResult.HttpError -> {
                    val message = when (result.code) {
                        401 -> "Invalid credentials"
                        404 -> "User not found"
                        else -> "Server error (${result.code})"
                    }
                    withContext(Dispatchers.Main) { view.showLoginError(message) }
                }
                is ApiResult.NetworkError -> {
                    withContext(Dispatchers.Main) { view.showLoginError(result.message) }
                }
            }
        }
    }

    override fun checkSession() {
        if (!sessionManager.getAccessToken().isNullOrEmpty()) {
            view.navigateToHome()
        }
    }
}
