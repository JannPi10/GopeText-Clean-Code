package com.example.gopetext.auth.login

import android.util.Log
import com.example.gopetext.data.api.AuthService
import com.example.gopetext.data.api.LoginRequest
import com.example.gopetext.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class LoginPresenter(
    private val view: LoginContract.View,
    private val api: AuthService
) : LoginContract.Presenter {

    override fun login(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val loginResponse = api.login(LoginRequest(email, password))
                Log.d("LoginPresenter", "Login exitoso: $loginResponse")

                withContext(Dispatchers.Main) {
                    view.showLoginSuccess()
                }

            } catch (e: HttpException) {
                val errorMessage = when (e.code()) {
                    401 -> "Credenciales inválidas"
                    404 -> "Usuario no encontrado"
                    else -> "Error desconocido (${e.code()})"
                }
                Log.e("LoginPresenter", "Error HTTP: ${e.message()}", e)

                withContext(Dispatchers.Main) {
                    view.showLoginError(errorMessage)
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
}
