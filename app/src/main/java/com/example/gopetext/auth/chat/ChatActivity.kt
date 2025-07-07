package com.example.gopetext.auth.chat

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gopetext.R
import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.api.ChatService
import com.example.gopetext.data.api.SendMessageRequest
import com.example.gopetext.data.model.Message
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var inputMessage: EditText
    private lateinit var sendIcon: ImageView
    private lateinit var chatService: ChatService
    private val handler = Handler()
    private lateinit var messageUpdater: Runnable
    private var lastMessageContent: String? = null
    companion object {
        private const val UPDATE_INTERVAL = 5000L // 5 segundos
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        initViews()
        setupRecyclerView()
        setupListeners()

        chatService = ApiClient.retrofit.create(ChatService::class.java)

        loadMessages()
        startAutoUpdate()
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
            if (content.isNotEmpty()) {
                sendMessage(SendMessageRequest(content))
                inputMessage.text.clear()
            }
        }
    }

    private fun startAutoUpdate() {
        messageUpdater = object : Runnable {
            override fun run() {
                loadMessages()
                handler.postDelayed(this, UPDATE_INTERVAL)
            }
        }
        handler.postDelayed(messageUpdater, UPDATE_INTERVAL)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(messageUpdater)
    }

    private fun loadMessages() {
        chatService.getMessages().enqueue(object : Callback<List<Message>> {
            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                if (response.isSuccessful) {
                    val messages = response.body().orEmpty()

                    // Compara el último mensaje con el anterior
                    val latestMessage = messages.lastOrNull()
                    if (latestMessage != null && latestMessage.content != lastMessageContent) {
                        if (lastMessageContent != null) {
                            showNotification("Nuevo mensaje", latestMessage.content)
                        }
                        lastMessageContent = latestMessage.content
                    }

                    adapter.setMessages(messages)
                    recyclerView.scrollToPosition(messages.size - 1)
                } else {
                    showToast("Error al cargar mensajes")
                }
            }

            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                showToast("Fallo de red")
            }
        })
    }
    private fun showNotification(title: String, message: String) {
        val channelId = "chat_channel"
        val notificationId = 1

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                channelId,
                "Canal de Chat",
                android.app.NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, ChatActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = androidx.core.app.NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_chats)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
    }




    private fun sendMessage(request: SendMessageRequest) {
        chatService.sendMessage(request).enqueue(object : Callback<Message> {
            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        adapter.addMessage(it)
                        recyclerView.scrollToPosition(adapter.itemCount - 1)
                    }
                } else {
                    showToast("Error al enviar mensaje")
                }
            }

            override fun onFailure(call: Call<Message>, t: Throwable) {
                showToast("Fallo de red")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
