package com.example.gopetext.auth.register

import com.example.gopetext.core.ApiResult
import com.example.gopetext.data.repository.UserRegistrationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterPresenter(
    private val view: RegisterContract.View,
    private val repository: UserRegistrationRepository
) : RegisterContract.Presenter {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun register(
        firstName: String,
        lastName: String,
        age: Int,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        scope.launch {
            when (val result = repository.registerUser(firstName, lastName, age, email, password, confirmPassword)) {
                is ApiResult.Success -> {
                    val message = result.data.message
                    withContext(Dispatchers.Main) {
                        view.showRegisterSuccess(message)
                        view.navigateToLogin()
                    }
                }
                is ApiResult.HttpError -> {
                    val message = result.message.ifBlank { "Server error (${result.code})" }
                    withContext(Dispatchers.Main) { view.showRegisterError(message) }
                }
                is ApiResult.NetworkError -> {
                    withContext(Dispatchers.Main) { view.showRegisterError(result.message) }
                }
            }
        }
    }
}
