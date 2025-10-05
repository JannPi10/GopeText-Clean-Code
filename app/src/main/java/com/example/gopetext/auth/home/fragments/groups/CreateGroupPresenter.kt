package com.example.gopetext.auth.home.fragments.groups

import com.example.gopetext.core.ApiResult
import com.example.gopetext.data.model.UserChat
import com.example.gopetext.data.repository.GroupRepository
import com.example.gopetext.data.repository.UsersRepository
import com.example.gopetext.data.storage.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateGroupPresenter(
    private val view: CreateGroupContract.View,
    private val usersRepository: UsersRepository,
    private val groupRepository: GroupRepository,
    private val sessionManager: SessionManager
) : CreateGroupContract.Presenter {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun loadUsers() {
        scope.launch {
            when (val result = usersRepository.getAllUsersForChat()) {
                is ApiResult.Success -> {
                    val currentUserId = sessionManager.getUserId()
                    val filteredUsers = result.data.filter { it.id != currentUserId }
                    withContext(Dispatchers.Main) { view.showUsers(filteredUsers) }
                }
                is ApiResult.HttpError -> withContext(Dispatchers.Main) {
                    view.showError("Unable to load users")
                }
                is ApiResult.NetworkError -> withContext(Dispatchers.Main) {
                    view.showError(result.message)
                }
            }
        }
    }

    override fun createGroup(name: String, selectedUsers: List<UserChat>) {
        if (name.isBlank()) {
            view.showError("Enter a group name")
            return
        }

        if (selectedUsers.isEmpty()) {
            view.showError("Select at least one person")
            return
        }

        val userIds = selectedUsers.map { it.id }

        scope.launch {
            when (val result = groupRepository.createGroup(name, userIds)) {
                is ApiResult.Success -> withContext(Dispatchers.Main) {
                    view.showSuccess("Group created successfully")
                    view.navigateBack()
                }
                is ApiResult.HttpError -> withContext(Dispatchers.Main) {
                    view.showError("Unable to create group")
                }
                is ApiResult.NetworkError -> withContext(Dispatchers.Main) {
                    view.showError(result.message)
                }
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
    }
}