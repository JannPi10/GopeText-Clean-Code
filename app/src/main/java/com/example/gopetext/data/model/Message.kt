package com.example.gopetext.data.model

data class Message(
    val id: Int,
    val content: String,
    val timestamp: String?,  // <-- antes era String, ahora puede ser null
    val isMine: Boolean
)



