package com.example.gopetext.auth.home

interface HomeContract {
    interface View {
        fun navigateToLogin()
        fun navigateToProfile()
        fun showLogoutMessage(message: String)
        fun showError(message: String)
    }

    interface Presenter {
        fun logout()
        fun profile()
    }
}
