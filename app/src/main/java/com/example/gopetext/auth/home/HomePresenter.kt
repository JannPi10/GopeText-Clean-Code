package com.example.gopetext.auth.home

import com.example.gopetext.core.ApiResult
import com.example.gopetext.data.repository.AccountRepository
import com.example.gopetext.data.storage.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomePresenter(
    private val view: HomeContract.View,
    private val accountRepository: AccountRepository,
    private val sessionManager: SessionManager
) : HomeContract.Presenter {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun logout() {
        scope.launch {
            val result = accountRepository.logout()
            sessionManager.clearSession()
            withContext(Dispatchers.Main) {
                when (result) {
                    is ApiResult.Success -> view.showLogoutMessage(result.data.message ?: "Session closed")
                    is ApiResult.HttpError -> view.showLogoutMessage("Sesion cerrada")
                    is ApiResult.NetworkError -> view.showLogoutMessage("Sesion cerrada")
                }
                view.navigateToLogin()
            }
        }
    }

    override fun profile() {
        scope.launch {
            withContext(Dispatchers.Main) { view.navigateToProfile() }
        }
    }
}
