package com.example.gopetext.auth.home.fragments.groups

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gopetext.R
import com.example.gopetext.data.model.User
import com.example.gopetext.data.model.UserChat
import com.example.gopetext.utils.Constants

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val users = mutableListOf<UserChat>()
    private val selectedUsers = mutableSetOf<UserChat>()

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkBoxUser)
        val tvUserName: TextView = view.findViewById(R.id.tvUserName)
        val imgUser: ImageView = view.findViewById(R.id.imgUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_checkbox, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.tvUserName.text = user.name

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = selectedUsers.contains(user)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedUsers.add(user)
                Log.d("CreateGroup", "Seleccionado: ${user.name}")
            } else {
                selectedUsers.remove(user)
                Log.d("CreateGroup", "Deseleccionado: ${user.name}")
            }
        }

        val imageUrl = user.profile_image_url?.let {
            if (it.startsWith("http")) it else Constants.BASE_URL + it.removePrefix("/")
        }

        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_baseline_person_24)
                .circleCrop()
                .into(holder.imgUser)
        } else {
            holder.imgUser.setImageResource(R.drawable.ic_baseline_person_24)
        }
    }

    fun setUsers(list: List<UserChat>) {
        users.clear()
        users.addAll(list)
        Log.d("CreateGroup", "Adapter actualizado con ${users.size} usuarios")
        notifyDataSetChanged()
    }

    fun getSelectedUsers(): List<UserChat> = selectedUsers.toList()
}



