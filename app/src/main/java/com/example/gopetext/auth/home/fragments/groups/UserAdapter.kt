package com.example.gopetext.auth.home.fragments.groups

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gopetext.R
import com.example.gopetext.data.model.UserChat
import com.example.gopetext.databinding.ItemUserCheckboxBinding
import com.example.gopetext.utils.Constants

class UserAdapter(
    private var users: List<UserChat>
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val selectedUsers = mutableSetOf<UserChat>()

    inner class UserViewHolder(private val binding: ItemUserCheckboxBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserChat) {
            binding.tvUserName.text = user.name

            binding.checkBoxUser.setOnCheckedChangeListener(null)
            binding.checkBoxUser.isChecked = selectedUsers.contains(user)

            binding.checkBoxUser.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) selectedUsers.add(user) else selectedUsers.remove(user)
            }

            val imageUrl = user.profile_image_url?.let {
                if (it.startsWith("http")) it else Constants.BASE_URL + it.removePrefix("/")
            }

            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_baseline_person_24)
                .circleCrop()
                .into(binding.imgUser)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserCheckboxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun setUsers(newUsers: List<UserChat>) {
        users = newUsers
        notifyDataSetChanged()
    }

    fun getSelectedUsers(): List<UserChat> = selectedUsers.toList()
}