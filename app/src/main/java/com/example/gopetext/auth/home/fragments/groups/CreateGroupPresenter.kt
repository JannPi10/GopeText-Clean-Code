package com.example.gopetext.auth.home.fragments.groups

import android.util.Log
import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.api.AuthService
import com.example.gopetext.data.model.User
import com.example.gopetext.data.model.UserChat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateGroupPresenter(
    private val view: CreateGroupContract.View
) : CreateGroupContract.Presenter {

    private val api = ApiClient.getService()

    override fun loadUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.getAllUsers()
                if (response.isSuccessful && response.body() != null) {
                    val users = response.body()!!.users
                    withContext(Dispatchers.Main) {
                        view.showUsers(users)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        view.showError("Error al cargar usuarios")
                    }
                }
            } catch (e: Exception) {
                Log.e("CreateGroup", "Error: ${e.message}")
                withContext(Dispatchers.Main) {
                    view.showError("Fallo de red")
                }
            }
        }
    }

    override fun createGroup(selectedUsers: List<UserChat>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Simulación de creación de grupo
                Log.d("CreateGroup", "Usuarios seleccionados: $selectedUsers")
                withContext(Dispatchers.Main) {
                    if (selectedUsers.isEmpty()) {
                        view.showSuccess("Selecciona primero una persona")
                    }
                    else {
                        view.showSuccess("Grupo creado con ${selectedUsers.size} personas")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view.showError("Error al crear grupo")
                }
            }
        }
    }
}
