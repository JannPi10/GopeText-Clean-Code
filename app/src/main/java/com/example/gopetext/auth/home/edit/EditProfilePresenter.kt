package com.example.gopetext.auth.home.edit

import android.util.Log
import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.api.AuthService
import com.example.gopetext.data.api.UpdateUserRequest
import com.example.gopetext.data.model.User
import com.example.gopetext.data.storage.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class EditProfilePresenter(
    private val view: EditProfileContract.View,
    private val sessionManager: SessionManager
) : EditProfileContract.Presenter {

    private val api: AuthService = ApiClient.getService()
    private var currentUser: User? = null

    override fun loadUserProfile() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.getUserProfile()
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!.user
                    currentUser = user

                    withContext(Dispatchers.Main) {
                        view.showUserData(user)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        view.showError("No se pudo cargar el perfil.")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view.showError("Error de red al cargar perfil.")
                }
            }
        }
    }

    override fun updateUserProfile(name: String, lastName: String, age: Int, photo: MultipartBody.Part?) {
        CoroutineScope(Dispatchers.IO).launch {
            val original = currentUser

            val sinCambios = original != null &&
                    original.name == name &&
                    original.last_name == lastName &&
                    original.age == age &&
                    photo == null

            if (sinCambios) {
                withContext(Dispatchers.Main) {
                    view.showSuccess("No se hicieron cambios.")
                    view.goBackProfile()
                }
                return@launch
            }

            try {
                Log.d("EditProfile", "Actualizando perfil: $name $lastName ($age años)")

                val request = UpdateUserRequest(name, lastName, age)
                val updateResponse = api.updateUserProfile(request)
                Log.d("EditProfile", "Respuesta updateUserProfile: ${updateResponse.code()} ${updateResponse.message()}")

                withContext(Dispatchers.Main) {
                    if (!updateResponse.isSuccessful) {
                        view.showError("Error al actualizar datos.")
                        return@withContext
                    }
                }

                if (photo != null) {
                    Log.d("EditProfile", "Subiendo imagen: ${photo.body.contentLength()} bytes")
                    val photoResponse = api.uploadProfileImage(photo)
                    Log.d("EditProfile", "Respuesta uploadProfileImage: ${photoResponse.code()} ${photoResponse.message()}")

                    withContext(Dispatchers.Main) {
                        if (!photoResponse.isSuccessful) {
                            view.showError("Error al subir imagen.")
                            return@withContext
                        }
                    }
                } else {
                    Log.d("EditProfile", "No se seleccionó imagen para subir.")
                }

                // Actualizar datos cacheados
                if (original != null) {
                    currentUser = original.copy(name = name, last_name = lastName, age = age)
                }

                withContext(Dispatchers.Main) {
                    view.showSuccess("Perfil actualizado correctamente.")
                    view.goBackProfile()
                }

            } catch (e: Exception) {
                Log.e("EditProfile", "Exception al actualizar perfil", e)
                withContext(Dispatchers.Main) {
                    view.showError("Error al actualizar: ${e.message}")
                }
            }
        }
    }
}



