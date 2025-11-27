package com.example.gopetext.auth.home.users.chat

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gopetext.R
import com.example.gopetext.auth.chat.ChatAdapter
import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.api.ChatService
import com.example.gopetext.data.api.SendMessageRequest
import com.example.gopetext.data.model.Message
import com.example.gopetext.data.repository.RemoteChatRepository
import com.example.gopetext.data.storage.SessionManager
import com.example.gopetext.databinding.ActivityChatBinding

class ChatSingleActivity : AppCompatActivity(), ChatSingleContract.View {

    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: ChatAdapter
    private lateinit var presenter: ChatSingleContract.Presenter
    private lateinit var sessionManager: SessionManager
    private var chatId: Int = -1
    private var isGroup: Boolean = false
    private var chatName: String? = null
    private var currentUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("ChatSingleActivity", "onCreate - Iniciando actividad")
        sessionManager = SessionManager(this)
        
        // Obtener el ID del usuario actual
        currentUserId = sessionManager.getUserId()
        Log.d("ChatSingleActivity", "ID de usuario actual: $currentUserId")
        
        // Verificar si el token está disponible
        val token = sessionManager.getAccessToken()
        Log.d("ChatSingleActivity", "Token de acceso: ${token?.take(10)}...")

        chatId = intent.getIntExtra("chatId", -1)
        isGroup = intent.getBooleanExtra("isGroup", false)
        chatName = intent.getStringExtra("chatName")

        if (chatId <= 0) {
            Toast.makeText(this, "Invalid chat", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        presenter = ChatSinglePresenter(
            this,
            RemoteChatRepository(ApiClient.createService<ChatService>())
        )
        presenter.setChatId(chatId)

        initViews()
        setupRecycler()
        setupListeners()
        presenter.loadMessages()
    }

    private fun initViews() {
        binding.textName.text = chatName ?: "Chat"
        binding.imageBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        if (isGroup) {
            binding.btnLeaveGroup.visibility = View.VISIBLE
            binding.btnLeaveGroup.setOnClickListener { showLeaveGroupConfirmation() }
        } else {
            binding.btnLeaveGroup.visibility = View.GONE
        }
    }

    private fun setupRecycler() {
        Log.d("ChatSingleActivity", "Configurando Recycler con currentUserId: $currentUserId")
        adapter = ChatAdapter(currentUserId)
        binding.chatRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerview.adapter = adapter
    }

    private fun setupListeners() {
        binding.sendIcon.setOnClickListener {
            val content = binding.inputMessage.text.toString().trim()
            if (content.isNotEmpty()) {
                presenter.sendMessage(chatId, SendMessageRequest(content))
                binding.inputMessage.text?.clear()
            }
        }
    }

    private fun showLeaveGroupConfirmation() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Abandonar grupo")
            .setMessage("¿Estás seguro de que quieres salir del grupo?")
            .setPositiveButton("Sí") { _, _ -> presenter.leaveGroup(chatId) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun showMessages(messages: List<Message>) {
        adapter.setMessages(messages)
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun scrollToBottom() {
        binding.chatRecyclerview.scrollToPosition(adapter.itemCount - 1)
    }

    override fun showNotification(title: String, message: String) {
        val channelId = "chat_channel"
        val notificationId = 1
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Canal de Chat", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_chats)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        notificationManager.notify(notificationId, builder.build())
    }

    override fun onLeftGroup() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
