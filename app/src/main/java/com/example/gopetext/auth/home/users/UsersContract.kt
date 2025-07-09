package com.example.gopetext.auth.home.users

import com.example.gopetext.data.model.UserChat

interface UsersContract {
    interface View {
        fun showUsers(users: List<UserChat>)
        fun showError(message: String)
        fun navigateToChat(chatId: Int, user: UserChat)
    }

    interface Presenter {
        fun loadUsers()
        fun onUserClicked(user: UserChat)
    }
}
