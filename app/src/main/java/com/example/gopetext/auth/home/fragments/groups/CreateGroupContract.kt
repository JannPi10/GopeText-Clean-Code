package com.example.gopetext.auth.home.fragments.groups

import com.example.gopetext.data.model.UserChat

interface CreateGroupContract {
    interface View {
        fun showUsers(users: List<UserChat>)
        fun showSuccess(message: String)
        fun showError(message: String)
        fun navigateBack()
    }

    interface Presenter {
        fun loadUsers()
        fun createGroup(name: String, selectedUsers: List<UserChat>)
        fun onDestroy()
    }
}