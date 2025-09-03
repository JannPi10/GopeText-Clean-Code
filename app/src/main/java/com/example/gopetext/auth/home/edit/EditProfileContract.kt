package com.example.gopetext.auth.home.edit

import com.example.gopetext.data.model.User
import okhttp3.MultipartBody

interface EditProfileContract {
    interface View {
        fun showUserData(user: User)
        fun showSuccess(message: String)
        fun showError(message: String)
        fun goBackProfile()
    }

    interface Presenter {
        fun loadUserProfile()
        fun updateUserProfile(name: String, lastName: String, age: Int, photo: MultipartBody.Part?)
        fun onDestroy() // Añadido para manejo del ciclo de vida
    }
}



