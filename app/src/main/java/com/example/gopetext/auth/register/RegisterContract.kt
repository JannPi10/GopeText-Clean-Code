package com.example.gopetext.auth.register

interface RegisterContract {
    interface View {
        fun showRegisterSuccess(message: String)
        fun showRegisterError(message: String)
        fun navigateToLogin()
    }
    interface Presenter {
        fun register(
            firstName: String,
            lastName: String,
            age: Int,
            email: String,
            password: String,
            confirmPassword: String
        )
    }
}
