package com.example.gopetext.auth.home.fragments.chats

import android.util.Log
import com.example.gopetext.data.api.AuthService
import com.example.gopetext.data.api.ChatService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatsPresenter(
    private val view: ChatsContract.View,
    private val chatService: ChatService
) : ChatsContract.Presenter {

    override fun loadChats() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("ChatsPresenter", "Solicitando chats...")
                val response = chatService.getChats()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val chats = response.body()?.chats
                        Log.d("ChatsPresenter", "Chats recibidos: $chats")

                        if (chats.isNullOrEmpty()) {
                            view.showEmptyState()
                        } else {
                            view.showChats(chats)
                        }
                    } else {
                        val error = response.errorBody()?.string()
                        Log.e("ChatsPresenter", "Error ${response.code()}: ${response.message()}")
                        Log.e("ChatsPresenter", "Cuerpo del error: $error")
                        view.showError("Error ${response.code()}: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("ChatsPresenter", "Excepción al obtener los chats", e)
                withContext(Dispatchers.Main) {
                    view.showError("Error al obtener los chats: ${e.localizedMessage}")
                }
            }
        }
    }
}

