package com.example.gopetext.auth.login

interface LoginContract {
    interface View {
        fun showLoginSuccess()
        fun showLoginError(message: String)
    }

    interface Presenter {
        fun login(email: String, password: String)
    }
}