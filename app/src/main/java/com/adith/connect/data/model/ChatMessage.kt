package com.adith.connect.data.model

data class ChatMessage(
    val text: String = "",
    val sender: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
