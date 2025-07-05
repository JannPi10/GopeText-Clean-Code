package com.example.gopetext.auth.home.fragments.chats

import com.example.gopetext.data.api.AuthService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatsPresenter(
    private val view: ChatsContract.View,
    private val authService: AuthService
) : ChatsContract.Presenter {

    override fun loadChats() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = authService.getChats() //
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val chats = response.body()
                        if (chats.isNullOrEmpty()) view.showEmptyState()
                        else view.showChats(chats)
                    } else {
                        view.showError("Error ${response.code()}: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view.showError("Error al obtener los chats")
                }
            }
        }
    }
}
