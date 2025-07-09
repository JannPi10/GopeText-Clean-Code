package com.example.gopetext.auth.home.users.chat

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.api.ChatService
import com.example.gopetext.data.api.SendMessageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChatPresenter(
    private val view: ChatContract.View
) : ChatContract.Presenter {

    private var chatId: Int = -1
    private var isActive = true
    private val handler = Handler(Looper.getMainLooper())
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val chatService: ChatService = ApiClient.retrofit.create(ChatService::class.java)

    override fun setChatId(chatId: Int) {
        this.chatId = chatId
        Log.d("ChatPresenter", "Chat ID establecido: $chatId")
    }

    override fun loadMessages() {
        if (chatId <= 0) {
            Log.e("ChatPresenter", "chatId inválido: $chatId")
            return
        }

        scope.launch {
            try {
                val response = chatService.getMessages(chatId)
                val messages = response.messages

                Log.d("ChatPresenter", "Mensajes obtenidos: ${messages.size}")

                withContext(Dispatchers.Main) {
                    if (isActive) {
                        view.showMessages(messages)
                        view.scrollToBottom()
                    }
                }

            } catch (e: Exception) {
                Log.e("ChatPresenter", "Excepción al cargar mensajes", e)
                withContext(Dispatchers.Main) {
                    view.showError("Error al cargar mensajes: ${e.localizedMessage}")
                }
            }
        }

        // Evitamos múltiples recargas paralelas si el handler ya está activo
        handler.removeCallbacksAndMessages(null)

        handler.postDelayed({
            if (isActive) {
                Log.d("ChatPresenter", "Recargando mensajes automáticamente")
                loadMessages()
            }
        }, 5000)
    }

    override fun sendMessage(chatId: Int, message: SendMessageRequest) {
        Log.d("ChatPresenter", "Enviando mensaje: ${message.message}")

        scope.launch {
            try {
                val response = chatService.sendMessage(chatId, message)

                if (response.isSuccessful) {
                    Log.d("ChatPresenter", "Mensaje enviado correctamente")
                    loadMessages()
                } else {
                    Log.e("ChatPresenter", "Error al enviar mensaje: ${response.code()} ${response.errorBody()?.string()}")
                    withContext(Dispatchers.Main) {
                        view.showError("No se pudo enviar el mensaje")
                    }
                }

            } catch (e: Exception) {
                Log.e("ChatPresenter", "Excepción al enviar mensaje", e)
                withContext(Dispatchers.Main) {
                    view.showError("Error al enviar mensaje: ${e.localizedMessage}")
                }
            }
        }
    }

    override fun onDestroy() {
        Log.d("ChatPresenter", "Presenter destruido. Cancelando corutinas y Handler.")
        isActive = false
        handler.removeCallbacksAndMessages(null)
        scope.cancel()
    }
}





