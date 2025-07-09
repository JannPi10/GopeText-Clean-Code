package com.example.gopetext.auth.home.users.chat

import com.example.gopetext.data.api.SendMessageRequest
import com.example.gopetext.data.model.Message

interface ChatContract {
    interface View {
        fun showMessages(messages: List<Message>)
        fun showError(message: String)
        fun scrollToBottom()
        fun showNotification(title: String, message: String)
    }

    interface Presenter {
        fun loadMessages()
        fun sendMessage(chatId: Int, message: SendMessageRequest)
        fun setChatId(chatId: Int)  // ✅ Añadir esto si no estaba
        fun onDestroy()             // ✅ Añadir esto
    }
}




