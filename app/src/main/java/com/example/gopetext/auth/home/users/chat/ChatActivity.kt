package com.example.gopetext.auth.home.users.chat

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
import com.example.gopetext.auth.chat.ChatAdapter
import com.example.gopetext.data.api.SendMessageRequest
import com.example.gopetext.data.model.Message

class ChatActivity : AppCompatActivity(), ChatContract.View {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var inputMessage: EditText
    private lateinit var sendIcon: ImageView
    private lateinit var presenter: ChatContract.Presenter
    private var chatId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatId = intent.getIntExtra("chatId", -1)

        if (chatId <= 0) {
            Log.e("ChatActivity", "chatId inválido recibido: $chatId")
            Toast.makeText(this, "Error: Chat no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        presenter = ChatPresenter(this)
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

        findViewById<AppCompatImageView>(R.id.imageBack).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        adapter = ChatAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        sendIcon.setOnClickListener {
            val content = inputMessage.text.toString().trim()
            Log.d("ChatActivity", "Click en enviar - mensaje: $content")

            if (content.isNotEmpty()) {
                presenter.sendMessage(chatId, SendMessageRequest(content))
                inputMessage.text.clear()
            }
        }
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

    override fun onDestroy() {
        if (this::presenter.isInitialized) {
            presenter.onDestroy()
        }
        super.onDestroy()
    }
}


