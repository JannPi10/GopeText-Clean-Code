package com.example.gopetext.auth.home.fragments.profile

import android.util.Log
import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.api.AuthService
import com.example.gopetext.data.api.UpdateUserRequest
import com.example.gopetext.data.storage.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProfilePresenter(
    private val view: ProfileContract.View,
    private val sessionManager: SessionManager
) : ProfileContract.Presenter {

    private val api: AuthService = ApiClient.getService()

    override fun loadUserData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("DEBUG", "Llamando a getUserProfile()")
                val response = api.getUserProfile()
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!.user
                    withContext(Dispatchers.Main) {
                        view.showUserData(user)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        view.showError("Error al cargar el perfil")
                    }
                }
            } catch (e: Exception) {
                Log.e("ProfilePresenter", "Exception: ${e.localizedMessage}")
                withContext(Dispatchers.Main) {
                    view.showError("Error de red al cargar perfil")
                }
            }
        }
    }

    override fun showUser(name: String, lastName: String, age: Int, photo: MultipartBody.Part?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Actualizar texto como JSON
                val request = UpdateUserRequest(name, lastName, age)
                val userResponse = api.updateUserProfile(request)

                withContext(Dispatchers.Main) {
                    if (userResponse.isSuccessful) {
                        view.showUpdateSuccess("Perfil actualizado correctamente")
                    } else {
                        view.showError("Error al actualizar el perfil")
                    }
                }

                // Actualizar imagen si existe
                if (photo != null) {
                    val imageResponse = api.uploadProfileImage(photo)

                    withContext(Dispatchers.Main) {
                        if (imageResponse.isSuccessful && imageResponse.body() != null) {
                            view.showUpdateSuccess("Foto de perfil actualizada")
                        } else {
                            view.showError("Error al subir la imagen")
                        }
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view.showError("Error: ${e.localizedMessage}")
                }
            }
        }
    }

}



