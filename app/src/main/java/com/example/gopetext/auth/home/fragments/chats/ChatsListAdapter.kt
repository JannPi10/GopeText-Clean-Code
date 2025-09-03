package com.example.gopetext.auth.home.fragments.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gopetext.R
import com.example.gopetext.data.model.Contact
import com.example.gopetext.utils.UrlUtils.imageUrlBuilder
import com.example.gopetext.utils.loadImage

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

        bindName(holder, contact)
        bindImage(holder, contact)
        bindClick(holder, contact)
    }

    private fun bindName(holder: ChatViewHolder, contact: Contact) {
        holder.name.text = contact.name
    }

    private fun bindImage(holder: ChatViewHolder, contact: Contact) {
        val imageUrl = imageUrlBuilder(contact.profile_image_url)
        holder.image.loadImage(imageUrl)
    }

    private fun bindClick(holder: ChatViewHolder, contact: Contact) {
        holder.itemView.setOnClickListener { onItemClick(contact) }
    }

    class DiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Contact, newItem: Contact) = oldItem == newItem
    }
}

