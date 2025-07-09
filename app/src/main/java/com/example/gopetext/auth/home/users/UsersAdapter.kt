package com.example.gopetext.auth.home.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gopetext.R
import com.example.gopetext.data.model.UserChat
import com.example.gopetext.databinding.ItemUserBinding
import com.example.gopetext.utils.Constants

class UsersAdapter(
    private var userList: List<UserChat>,
    private val onClick: (UserChat) -> Unit
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    inner class UserViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserChat) {
            binding.tvUserName.text = user.name

            val imageUrl = user.profile_image_url?.let {
                if (it.startsWith("http")) it else Constants.BASE_URL + it.removePrefix("/")
            }

            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_baseline_person_24)
                .circleCrop()
                .into(binding.imgUser)

            binding.root.setOnClickListener { onClick(user) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int = userList.size

    fun updateList(newList: List<UserChat>) {
        userList = newList
        notifyDataSetChanged()
    }
}

