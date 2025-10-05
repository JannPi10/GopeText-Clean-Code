package com.example.gopetext.auth.home.edit

import com.example.gopetext.data.model.User
import com.example.gopetext.data.repository.UserRepository
import com.example.gopetext.utils.Result
import kotlinx.coroutines.*
import okhttp3.MultipartBody

class EditProfilePresenter(
    private val view: EditProfileContract.View,
    private val userRepository: UserRepository,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : EditProfileContract.Presenter {

    private val presenterJob = SupervisorJob()
    private val presenterScope = CoroutineScope(mainDispatcher + presenterJob)
    var currentUser: User? = null

    override fun loadUserProfile() {
        presenterScope.launch {
            when (val result = withContext(ioDispatcher) { userRepository.getUserProfile() }) {
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

            when (val result = withContext(ioDispatcher) {
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

