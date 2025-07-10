package com.example.gopetext.auth.home.fragments.groups

import android.util.Log
import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.api.CreateGroupRequest
import com.example.gopetext.data.model.UserChat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateGroupPresenter(
    private val view: CreateGroupContract.View
) : CreateGroupContract.Presenter {

    private val api = ApiClient.getService()

    override fun loadUsers(currentUserId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.getAllUsers()
                if (response.isSuccessful) {
                    val allUsers = response.body()?.users ?: emptyList()
                    val filtered = allUsers.filter { it.id != currentUserId }

                    Log.d("CreateGroup", "Usuarios recibidos: ${allUsers.size}")
                    Log.d("CreateGroup", "Usuarios después de filtrar actual: ${filtered.size}")

                    withContext(Dispatchers.Main) {
                        view.showUsers(filtered)
                    }
                } else {
                    Log.e("CreateGroup", "Fallo al obtener usuarios: ${response.code()}")
                    withContext(Dispatchers.Main) {
                        view.showError("No se pudieron cargar los usuarios")
                    }
                }
            } catch (e: Exception) {
                Log.e("CreateGroup", "Error al obtener usuarios: ${e.message}")
                withContext(Dispatchers.Main) {
                    view.showError("Error de red: ${e.message}")
                }
            }
        }
    }

    override fun createGroup(name: String, selectedUsers: List<UserChat>) {
        if (name.isBlank()) {
            view.showError("Ingresa un nombre para el grupo")
            return
        }

        if (selectedUsers.isEmpty()) {
            view.showError("Selecciona al menos una persona")
            return
        }

        val userIds = selectedUsers.map { it.id }
        val request = CreateGroupRequest(name = name, members = userIds)

        Log.d("CreateGroup", "Intentando crear grupo con: $request")

        Log.d("CreateGroupRequest", "Payload: name=${request.name}, members=${request.members}")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.createGroup(request)
                if (response.isSuccessful) {
                    Log.d("CreateGroup", "Grupo creado con éxito")
                    withContext(Dispatchers.Main) {
                        view.showSuccess("Grupo creado correctamente")
                    }
                } else {
                    Log.e("CreateGroup", "Error HTTP al crear grupo: ${response.code()}")
                    withContext(Dispatchers.Main) {
                        view.showError("No se pudo crear el grupo (código: ${response.code()})")
                    }
                }
            } catch (e: Exception) {
                Log.e("CreateGroup", "Excepción al crear grupo: ${e.message}")
                withContext(Dispatchers.Main) {
                    view.showError("Error de red: ${e.message}")
                }
            }
        }
    }
}


