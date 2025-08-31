package com.example.gopetext.auth.home.users

import com.example.gopetext.core.ApiResult
import com.example.gopetext.data.model.UserChat
import com.example.gopetext.data.repository.ChatRepository
import com.example.gopetext.data.repository.UsersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsersPresenter(
    private val view: UsersContract.View,
    private val usersRepository: UsersRepository,
    private val chatRepository: ChatRepository
) : UsersContract.Presenter {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun loadUsers() {
        scope.launch {
            when (val result = usersRepository.getAllUsersForChat()) {
                is ApiResult.Success -> withContext(Dispatchers.Main) { view.showUsers(result.data) }
                is ApiResult.HttpError -> withContext(Dispatchers.Main) { view.showError("Unable to load users") }
                is ApiResult.NetworkError -> withContext(Dispatchers.Main) { view.showError(result.message) }
            }
        }
    }

    override fun onUserClicked(user: UserChat) {
        scope.launch {
            when (val result = chatRepository.createChatWith(user.id)) {
                is ApiResult.Success -> withContext(Dispatchers.Main) { view.navigateToChat(result.data, user) }
                is ApiResult.HttpError -> withContext(Dispatchers.Main) { view.showError("Unable to create chat") }
                is ApiResult.NetworkError -> withContext(Dispatchers.Main) { view.showError(result.message) }
            }
        }
    }
}
