package com.example.gopetext.auth.home.fragments.chats

import com.example.gopetext.data.model.Contact

interface ChatsNavigator {
    fun navigateToChat(contact: Contact)
}