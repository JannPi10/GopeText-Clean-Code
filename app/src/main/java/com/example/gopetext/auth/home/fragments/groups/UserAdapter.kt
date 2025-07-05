package com.example.gopetext.auth.home.fragments.groups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gopetext.R
import com.example.gopetext.data.model.User
import com.example.gopetext.data.model.UserChat

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val users = mutableListOf<UserChat>()
    private val selectedUsers = mutableSetOf<UserChat>()

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkBoxUser)
        val tvUserName: TextView = view.findViewById(R.id.tvUserName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_checkbox, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]

        // ✅ Muestra el nombre en el TextView
        holder.tvUserName.text = user.name

        // ✅ Limpiar listener viejo para evitar efectos raros
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = selectedUsers.contains(user)

        // ✅ Asignar nuevo listener
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selectedUsers.add(user)
            else selectedUsers.remove(user)
        }
    }

    fun setUsers(list: List<UserChat>) {
        users.clear()
        users.addAll(list)
        notifyDataSetChanged()
    }

    fun getSelectedUsers(): List<UserChat> = selectedUsers.toList()
}

