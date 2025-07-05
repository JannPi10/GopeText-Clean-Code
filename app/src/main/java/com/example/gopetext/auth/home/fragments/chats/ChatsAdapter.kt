package com.example.gopetext.auth.home.fragments.chats

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

class ChatsAdapter(
    private val onItemClick: (Contact) -> Unit
) : ListAdapter<Contact, ChatsAdapter.ChatViewHolder>(DiffCallback()) {

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
        holder.name.text = contact.name
        Glide.with(holder.itemView.context).load(contact.profileImageUrl).into(holder.image)
        holder.itemView.setOnClickListener { onItemClick(contact) }
    }

    class DiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(old: Contact, new: Contact) = old.id == new.id
        override fun areContentsTheSame(old: Contact, new: Contact) = old == new
    }
}
