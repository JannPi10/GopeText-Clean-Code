package com.example.gopetext.auth.register

import com.example.gopetext.data.api.*
import com.example.gopetext.utils.Constants
import com.example.gopetext.utils.ErrorUtils
import kotlinx.coroutines.*
import android.util.Log

class RegisterPresenter(private val view: RegisterContract.View) : RegisterContract.Presenter {

    private val service = ApiClient.retrofit.create(AuthService::class.java)

    override fun register(
        name: String,
        last_name: String,
        age: Int,
        email: String,
        password: String,
        confirm_password: String
    ) {
        Log.d("RegisterPresenter", "Preparando registro...")
        Log.d("RegisterPresenter", "Datos recibidos -> nombre: $name, apellido: $last_name, edad: $age, email: $email")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = RegisterRequest(name, last_name, age, email, password, confirm_password)
                Log.d("RegisterPresenter", "Enviando solicitud al servidor: $request")

                // 🔍 Log adicional para confirmar la URL final usada por Retrofit
                Log.d("Retrofit", "URL final: ${ApiClient.retrofit.baseUrl()}register")

                val response = service.register(request)

                withContext(Dispatchers.Main) {
                    Log.d("RegisterPresenter", "Respuesta recibida. Código: ${response.code()}")

                    if (response.isSuccessful) {
                        val message = response.body()?.message ?: Constants.DEFAULT_SUCCESS
                        Log.d("RegisterPresenter", "Registro exitoso: $message")
                        view.showMessage(message)
                    } else {
                        val errorJson = response.errorBody()?.string()
                        val errorResponse = ErrorUtils.parseError(errorJson)

                        if (errorResponse != null) {
                            Log.e("RegisterPresenter", "Error del servidor: ${errorResponse.error.message}")
                            view.showRegisterFail("Error: ${errorResponse.error.message}")
                        } else {
                            Log.e("RegisterPresenter", "No se pudo parsear error: $errorJson")
                            view.showRegisterFail(Constants.UNKNOWN_ERROR)
                        }
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("RegisterPresenter", "Excepción capturada en registro", e)
                    view.showRegisterFail("${Constants.NETWORK_ERROR}${e.localizedMessage}")
                }
            }
        }
    }
}
