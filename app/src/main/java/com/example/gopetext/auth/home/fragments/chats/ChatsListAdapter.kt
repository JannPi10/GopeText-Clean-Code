package com.example.gopetext.auth.home.fragments.chats

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gopetext.R
import com.example.gopetext.data.model.Contact
import com.example.gopetext.utils.Constants

class ChatsListAdapter(
    private val onItemClick: (Contact) -> Unit
) : ListAdapter<Contact, ChatsListAdapter.ChatViewHolder>(DiffCallback()) {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.txtUserName)
        val image: ImageView = itemView.findViewById(R.id.imgUserPhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val contact = getItem(position)
        Log.d("ChatsAdapter", "Mostrando chat: ${contact.name}")

        holder.name.text = contact.name

        val imageUrl = contact.profile_image_url?.let {
            if (it.startsWith("http")) it else Constants.BASE_URL + it.removePrefix("/")
        }

        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.ic_baseline_person_24)
        }

        holder.itemView.setOnClickListener {
            onItemClick(contact)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Contact, newItem: Contact) = oldItem == newItem
    }
}


