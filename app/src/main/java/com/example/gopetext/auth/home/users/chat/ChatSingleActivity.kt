package com.example.gopetext.auth.home.users.chat

import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gopetext.R
import android.view.View
import android.widget.TextView
import com.example.gopetext.auth.chat.ChatAdapter
import com.example.gopetext.data.api.SendMessageRequest
import com.example.gopetext.data.model.Message
import com.example.gopetext.data.storage.SessionManager

class ChatSingleActivity : AppCompatActivity(), ChatSingleContract.View {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var inputMessage: EditText
    private lateinit var sendIcon: ImageView
    private lateinit var textName: TextView
    private lateinit var btnLeaveGroup: ImageView
    private lateinit var presenter: ChatSingleContract.Presenter
    private lateinit var sessionManager: SessionManager
    private var chatId: Int = -1
    private var isGroup: Boolean = false
    private var chatName: String? = null
    private var currentUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        sessionManager = SessionManager(this)

        val rawPrefs = getSharedPreferences("gopetext_prefs", MODE_PRIVATE)
        val storedId = rawPrefs.getInt("user_id", -1)
        Log.d("ChatActivity", "Verificando directamente: user_id desde prefs = $storedId")

        currentUserId = sessionManager.getUserId()
        Log.d("ChatActivity", "Usuario actual cargado: $currentUserId")

        chatId = intent.getIntExtra("chatId", -1)
        isGroup = intent.getBooleanExtra("isGroup", false)
        chatName = intent.getStringExtra("chatName")

        if (currentUserId == -1) {
            val retryId = sessionManager.getUserId()
            Log.w("ChatActivity", "userId inicial era -1, recuperado como: $retryId")
            currentUserId = retryId
        }

        if (chatId <= 0) {
            Log.e("ChatActivity", "chatId inválido recibido: $chatId")
            Toast.makeText(this, "Error: Chat no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        presenter = ChatSinglePresenter(this)
        presenter.setChatId(chatId)

        initViews()
        setupRecyclerView()
        setupListeners()
        presenter.loadMessages()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.chatRecyclerview)
        inputMessage = findViewById(R.id.inputMessage)
        sendIcon = findViewById(R.id.sendIcon)
        textName = findViewById(R.id.textName)
        btnLeaveGroup = findViewById(R.id.btnLeaveGroup)

        textName.text = chatName ?: "Chat"

        findViewById<AppCompatImageView>(R.id.imageBack).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (isGroup) {
            btnLeaveGroup.visibility = View.VISIBLE
            btnLeaveGroup.setOnClickListener { showLeaveGroupConfirmation() }
        } else {
            btnLeaveGroup.visibility = View.GONE
        }
    }

    private fun setupRecyclerView() {
        adapter = ChatAdapter(currentUserId)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        sendIcon.setOnClickListener {
            val content = inputMessage.text.toString().trim()
            if (content.isNotEmpty()) {
                presenter.sendMessage(chatId, SendMessageRequest(content))
                inputMessage.text.clear()
            }
        }
    }

    private fun showLeaveGroupConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Abandonar grupo")
            .setMessage("¿Estás seguro de que quieres salir del grupo?")
            .setPositiveButton("Sí") { _, _ ->
                presenter.leaveGroup(chatId)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun showMessages(messages: List<Message>) {
        Log.d("ChatActivity", "Mostrando ${messages.size} mensajes")
        adapter.setMessages(messages)
    }

    override fun showError(message: String) {
        Log.e("ChatActivity", "Error mostrado al usuario: $message")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun scrollToBottom() {
        recyclerView.scrollToPosition(adapter.itemCount - 1)
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
        if (this::presenter.isInitialized) {
            presenter.onDestroy()
        }
        super.onDestroy()
    }
}


