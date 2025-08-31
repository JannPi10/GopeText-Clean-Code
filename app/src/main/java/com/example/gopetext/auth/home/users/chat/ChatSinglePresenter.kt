package com.example.gopetext.auth.home.users.chat

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.api.ChatService
import com.example.gopetext.data.api.CreateGroupRequest
import com.example.gopetext.data.api.SendMessageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChatSinglePresenter(
    private val view: ChatSingleContract.View
) : ChatSingleContract.Presenter {

    private var chatId: Int = -1
    private var isActive = true
    private val handler = Handler(Looper.getMainLooper())
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val chatService: ChatService = ApiClient.createService(ChatService::class.java)

    override fun setChatId(chatId: Int) {
        this.chatId = chatId
        Log.d("ChatPresenter", "Chat ID establecido: $chatId")
    }

    override fun loadMessages() {
        if (chatId <= 0) return

        scope.launch {
            try {
                val response = chatService.getMessages(chatId)
                val messages = response.messages

                withContext(Dispatchers.Main) {
                    if (isActive) {
                        view.showMessages(messages)
                        view.scrollToBottom()
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view.showError("Error al cargar mensajes: ${e.localizedMessage}")
                }
            }
        }

        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({ if (isActive) loadMessages() }, 5000)
    }

    override fun sendMessage(chatId: Int, message: SendMessageRequest) {
        scope.launch {
            try {
                val response = chatService.sendMessage(chatId, message)
                if (response.isSuccessful) {
                    loadMessages()
                } else {
                    withContext(Dispatchers.Main) {
                        view.showError("No se pudo enviar el mensaje")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view.showError("Error al enviar mensaje: ${e.localizedMessage}")
                }
            }
        }
    }

    override fun leaveGroup(chatId: Int) {
        scope.launch {
            try {
                val request = CreateGroupRequest(name = "none", members = listOf())
                val response = chatService.leaveGroup(chatId, request)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Log.d("ChatPresenter", "Saliste del grupo con éxito")
                        view.onLeftGroup()
                    } else {
                        Log.e("ChatPresenter", "Error al salir del grupo: ${response.code()}")
                        view.showError("No se pudo abandonar el grupo")
                    }
                }
            } catch (e: Exception) {
                Log.e("ChatPresenter", "Excepción al abandonar grupo", e)
                withContext(Dispatchers.Main) {
                    view.showError("Error: ${e.localizedMessage}")
                }
            }
        }
    }

    override fun onDestroy() {
        isActive = false
        handler.removeCallbacksAndMessages(null)
        scope.cancel()
    }
}






