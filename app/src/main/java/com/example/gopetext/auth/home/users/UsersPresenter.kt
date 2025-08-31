package com.example.gopetext.auth.home.users

import android.util.Log
import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.api.ChatService
import com.example.gopetext.data.api.CreateChatRequest
import com.example.gopetext.data.model.User
import com.example.gopetext.data.model.UserChat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsersPresenter(private val view: UsersContract.View) : UsersContract.Presenter {

    override fun loadUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.getService().getAllUsers()
                if (response.isSuccessful && response.body() != null) {
                    val allUsers = response.body()!!.users
                    val chatUsers = allUsers.map { user ->
                        UserChat(
                            id = user.id,
                            name = user.name,
                            profile_image_url = user.profile_image_url
                        )
                    }
                    withContext(Dispatchers.Main) {
                        view.showUsers(chatUsers)
                    }
                } else {
                    Log.e("UsersPresenter", "Error de respuesta: ${response.code()}")
                    withContext(Dispatchers.Main) {
                        view.showError("No se pudieron cargar los usuarios")
                    }
                }
            } catch (e: Exception) {
                Log.e("UsersPresenter", "Error al cargar usuarios", e)
                withContext(Dispatchers.Main) {
                    view.showError("Error de red: ${e.localizedMessage}")
                }
            }
        }
    }

    override fun onUserClicked(user: UserChat) {
        Log.d("UsersPresenter", "Usuario clickeado: ${user.name}")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.createService<ChatService>().createChat(CreateChatRequest(user.id))

                if (response.isSuccessful && response.body() != null) {
                    val chat = response.body()!!.id
                    Log.d("UsersPresenter", "Chat creado con ID: ${chat}")
                    withContext(Dispatchers.Main) {
                        view.navigateToChat(chat, user)
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("UsersPresenter", "Error creando chat: $error")
                    withContext(Dispatchers.Main) {
                        view.showError("No se pudo crear el chat")
                    }
                }
            } catch (e: Exception) {
                Log.e("UsersPresenter", "Excepción al crear chat", e)
                withContext(Dispatchers.Main) {
                    view.showError("Error de red: ${e.localizedMessage}")
                }
            }
        }
    }

}

