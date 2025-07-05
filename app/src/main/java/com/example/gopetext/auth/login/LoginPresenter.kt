package com.example.gopetext.auth.login

import android.util.Log
import com.example.gopetext.data.api.AuthService
import com.example.gopetext.data.api.LoginRequest
import com.example.gopetext.data.storage.SessionManager
import com.example.gopetext.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class LoginPresenter(
    private val view: LoginContract.View,
    private val api: AuthService,
    private val sessionManager: SessionManager
) : LoginContract.Presenter {

    override fun login(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("LoginPresenter", "Intentando login con email: $email")
            Log.d("LoginPresenter", "Intentando login con password: $password")

            val loginRequest = LoginRequest(email, password)
            Log.d("LoginDebug", "Body JSON que se enviará: $loginRequest")

            try {
                val response = api.login(loginRequest)
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    Log.d("LoginPresenter", "Login exitoso: token=${body.token}")

                    sessionManager.saveAccessToken(body.token)

                    withContext(Dispatchers.Main) {
                        view.showLoginSuccess()
                    }
                }
                else {
                    val errorMessage = when (response.code()) {
                        401 -> "Usuario o contraseña incorrectos"
                        404 -> "Usuario no encontrado"
                        else -> "Error desconocido (${response.code()})"
                    }

                    val errorBody = response.errorBody()?.string()
                    Log.e("LoginPresenter", "Error HTTP ${response.code()}: $errorBody")

                    withContext(Dispatchers.Main) {
                        view.showLoginError(errorMessage)
                    }
                }

            } catch (e: IOException) {
                Log.e("LoginPresenter", "Error de red: ${e.localizedMessage}", e)
                withContext(Dispatchers.Main) {
                    view.showLoginError("Error de conexión. Verifica tu red.")
                }

            } catch (e: Exception) {
                Log.e("LoginPresenter", "Excepción inesperada", e)
                withContext(Dispatchers.Main) {
                    view.showLoginError("${Constants.NETWORK_ERROR}${e.message}")
                }
            }
        }
    }

    override fun checkSession() {
        if (!sessionManager.getAccessToken().isNullOrEmpty()) {
            Log.d("LoginPresenter", "Sesión activa detectada.")
            view.navigateToHome()
        }
    }
}

