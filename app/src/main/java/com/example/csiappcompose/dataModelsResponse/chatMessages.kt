package com.example.csiappcompose.dataModelsResponse

data class chatMessages(
    val attachment: Any,
    val created_at: String,
    val id: Int,
    val message: String,
    val message_type: String,
    val room: Int,
    val sender: Sender
)