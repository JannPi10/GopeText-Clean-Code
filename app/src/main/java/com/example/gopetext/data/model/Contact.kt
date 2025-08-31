package com.example.gopetext.data.model

data class Contact(
    val id: Int,
    val is_group: Boolean,
    val name: String,
    val profile_image_url: String?,
    val user_id: Int?,
    val members_count: Int?
)

