package com.example.gopetext.auth.home.fragments.profile

import com.example.gopetext.data.model.User
import okhttp3.MultipartBody

interface ProfileContract {
    interface View {
        fun showUserData(user: User)
        fun showUpdateSuccess(message: String)
        fun showError(message: String)
    }

    interface Presenter {
        fun loadUserData()
        fun showUser(name: String, lastName: String, age : Int, photo: MultipartBody.Part?)
    }
}
