package com.example.gopetext.auth.home.fragments.profile

import android.util.Log
import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.api.AuthService
import com.example.gopetext.data.storage.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
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
                Log.d("DEBUG", "Respuesta: ${response.code()} - ${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!.user // ahora sí es un solo User, no una lista
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

    override fun showUser(name: String, lastName: String, age : Int, photo: MultipartBody.Part?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val nameBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
                val lastNameBody = lastName.toRequestBody("text/plain".toMediaTypeOrNull())
                val ageBody = age.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                val nameUpdate = api.showUserProfile(nameBody, lastNameBody, ageBody)

                withContext(Dispatchers.Main) {
                    if (nameUpdate.isSuccessful && nameUpdate.body() != null) {
                        view.showUpdateSuccess("Perfil")
                    } else {
                        view.showError("Error al mostrar el perfil")
                    }
                }

                if (photo != null) {
                    val photoUpdate = api.uploadProfileImage(photo)
                    withContext(Dispatchers.Main) {
                        if (photoUpdate.isSuccessful && photoUpdate.body() != null) {
                            view.showUpdateSuccess("Foto de perfil")
                        } else {
                            view.showError("Error al mostrar la foto")
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


