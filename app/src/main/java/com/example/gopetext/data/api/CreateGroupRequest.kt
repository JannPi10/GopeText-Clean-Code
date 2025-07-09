package com.example.gopetext.data.api

data class CreateGroupRequest(
    val name: String,
    val userIds: List<Int>
)

