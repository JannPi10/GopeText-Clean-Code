package com.example.gopetext.auth.home.edit

import com.example.gopetext.data.model.User
import com.example.gopetext.data.repository.UserRepository
import com.example.gopetext.data.storage.SessionManager
import com.example.gopetext.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class EditProfilePresenter(
    private val view: EditProfileContract.View,
    sessionManager: SessionManager
) : EditProfileContract.Presenter {

    private val userRepository = UserRepository(sessionManager)
    private val presenterJob = Job()
    private val presenterScope = CoroutineScope(Dispatchers.Main + presenterJob)
    private var currentUser: User? = null

    override fun loadUserProfile() {
        presenterScope.launch {
            when (val result = withContext(Dispatchers.IO) {
                userRepository.getUserProfile()
            }) {
                is Result.Success -> {
                    currentUser = result.data.user
                    view.showUserData(result.data.user)
                }
                is Result.Error -> view.showError(result.message)
            }
        }
    }

    override fun updateUserProfile(name: String, lastName: String, age: Int, photo: MultipartBody.Part?) {
        presenterScope.launch {
            if (hasNoChanges(name, lastName, age, photo)) {
                view.showSuccess("No se hicieron cambios.")
                view.goBackProfile()
                return@launch
            }

            when (val result = withContext(Dispatchers.IO) {
                userRepository.updateUserProfile(name, lastName, age, photo)
            }) {
                is Result.Success -> {
                    updateCurrentUser(name, lastName, age)
                    view.showSuccess("Perfil actualizado correctamente.")
                    view.goBackProfile()
                }
                is Result.Error -> view.showError(result.message)
            }
        }
    }

    private fun hasNoChanges(name: String, lastName: String, age: Int, photo: MultipartBody.Part?): Boolean {
        val original = currentUser ?: return false
        return original.name == name &&
                original.last_name == lastName &&
                original.age == age &&
                photo == null
    }

    private fun updateCurrentUser(name: String, lastName: String, age: Int) {
        currentUser = currentUser?.copy(name = name, last_name = lastName, age = age)
    }

    override fun onDestroy() {
        presenterJob.cancel()
    }
}

