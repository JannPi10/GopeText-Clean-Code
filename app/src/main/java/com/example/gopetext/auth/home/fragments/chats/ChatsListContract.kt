package com.example.gopetext.auth.home.fragments.chats

import com.example.gopetext.data.model.Contact

interface ChatsListContract {
    interface View {
        fun showChats(chats: List<Contact>)
        fun showError(message: String)
        fun showEmptyState()
    }

    interface Presenter {
        fun loadChats()
    }
}
