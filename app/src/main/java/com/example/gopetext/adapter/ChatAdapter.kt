package com.example.gopetext.auth.chat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gopetext.R
import com.example.gopetext.data.model.Message
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages: MutableList<Message> = mutableListOf()

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2

        private val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())

        fun formatTimestamp(raw: String?): String {
            val millis = try {
                raw?.toLong()  // Si es válido, úsalo
            } catch (e: Exception) {
                null
            } ?: System.currentTimeMillis()  // Si es null o no se puede convertir, usa la hora actual

            return timeFormatter.format(Date(millis))
        }
    }

    fun setMessages(newMessages: List<Message>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isMine) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = if (viewType == VIEW_TYPE_SENT)
            R.layout.item_container_sent_message
        else
            R.layout.item_container_received_message

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)

        return if (viewType == VIEW_TYPE_SENT)
            SentMessageViewHolder(view)
        else
            ReceivedMessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is BaseMessageViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    abstract class BaseMessageViewHolder(
        itemView: View,
        private val messageTextId: Int,
        private val datetimeTextId: Int
    ) : RecyclerView.ViewHolder(itemView) {

        private val textMessage: TextView = itemView.findViewById(messageTextId)
        private val textDatetime: TextView = itemView.findViewById(datetimeTextId)

        open fun bind(message: Message) {
            Log.d("ChatAdapter", "Mostrando mensaje: ${message.content}, timestamp: ${message.timestamp}")
            textMessage.text = message.content
            textDatetime.text = formatTimestamp(message.timestamp)
        }
    }

    class SentMessageViewHolder(view: View) : BaseMessageViewHolder(
        view,
        R.id.textMessageSent,
        R.id.textDatetime
    )

    class ReceivedMessageViewHolder(view: View) : BaseMessageViewHolder(
        view,
        R.id.textMessageReceive,
        R.id.textDatetime
    )
}


