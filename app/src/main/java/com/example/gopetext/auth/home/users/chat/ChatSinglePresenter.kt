package com.example.gopetext.auth.home.users.chat

import android.os.Handler
import android.os.Looper
import com.example.gopetext.core.ApiResult
import com.example.gopetext.data.api.SendMessageRequest
import com.example.gopetext.data.repository.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatSinglePresenter(
    private val view: ChatSingleContract.View,
    private val repository: ChatRepository
) : ChatSingleContract.Presenter {

    private var chatId: Int = -1
    private var isActive = true
    private val handler = Handler(Looper.getMainLooper())
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun setChatId(chatId: Int) {
        this.chatId = chatId
    }

    override fun loadMessages() {
        if (chatId <= 0) return
        scope.launch {
            when (val result = repository.fetchMessages(chatId)) {
                is ApiResult.Success -> withContext(Dispatchers.Main) {
                    if (isActive) {
                        view.showMessages(result.data)
                        view.scrollToBottom()
                    }
                }
                is ApiResult.HttpError -> withContext(Dispatchers.Main) { view.showError(result.message) }
                is ApiResult.NetworkError -> withContext(Dispatchers.Main) { view.showError(result.message) }
            }
        }
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({ if (isActive) loadMessages() }, 5000)
    }

    override fun sendMessage(chatId: Int, request: SendMessageRequest) {
        scope.launch {
            when (val result = repository.sendMessage(chatId, request)) {
                is ApiResult.Success -> loadMessages()
                is ApiResult.HttpError -> withContext(Dispatchers.Main) { view.showError("Unable to send message") }
                is ApiResult.NetworkError -> withContext(Dispatchers.Main) { view.showError(result.message) }
            }
        }
    }

    override fun leaveGroup(chatId: Int) {
        scope.launch {
            when (val result = repository.leaveGroup(chatId)) {
                is ApiResult.Success -> withContext(Dispatchers.Main) { view.onLeftGroup() }
                is ApiResult.HttpError -> withContext(Dispatchers.Main) { view.showError("Unable to leave group") }
                is ApiResult.NetworkError -> withContext(Dispatchers.Main) { view.showError(result.message) }
            }
        }
    }

    override fun onDestroy() {
        isActive = false
        handler.removeCallbacksAndMessages(null)
        scope.cancel()
    }
}
