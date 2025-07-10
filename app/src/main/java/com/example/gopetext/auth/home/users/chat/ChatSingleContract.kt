package com.example.gopetext.auth.home.users.chat

import com.example.gopetext.data.api.SendMessageRequest
import com.example.gopetext.data.model.Message

interface ChatSingleContract {
    interface View {
        fun showMessages(messages: List<Message>)
        fun showError(message: String)
        fun scrollToBottom()
        fun showNotification(title: String, message: String)
        fun onLeftGroup()
    }

    interface Presenter {
        fun setChatId(chatId: Int)
        fun loadMessages()
        fun sendMessage(chatId: Int, request: SendMessageRequest)
        fun leaveGroup(chatId: Int)
        fun onDestroy()
    }
}





