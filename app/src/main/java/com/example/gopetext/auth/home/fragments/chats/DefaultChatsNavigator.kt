package com.example.gopetext.auth.home.fragments.chats

import android.content.Context
import android.content.Intent
import com.example.gopetext.auth.home.users.chat.ChatSingleActivity
import com.example.gopetext.data.model.Contact

class DefaultChatsNavigator(private val context: Context) : ChatsNavigator {

    override fun navigateToChat(contact: Contact) {
        val intent = Intent(context, ChatSingleActivity::class.java).apply {
            putExtra(EXTRA_CHAT_ID, contact.id)
            putExtra(EXTRA_CHAT_NAME, contact.name)
            putExtra(EXTRA_IS_GROUP, contact.is_group)
        }
        context.startActivity(intent)
    }

    companion object {
        const val EXTRA_CHAT_ID = "chatId"
        const val EXTRA_CHAT_NAME = "chatName"
        const val EXTRA_IS_GROUP = "isGroup"
    }
}