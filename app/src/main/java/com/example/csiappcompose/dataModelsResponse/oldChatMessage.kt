package com.example.csiappcompose.dataModelsResponse

data class oldChatMessage(
    val attachment: Any?,
    val content: String,
    val created_at: String,
    val id: Int,
    val is_deleted: Boolean,
    val is_edited: Boolean,
    val mentions: List<Int>,
    val message_type: String,
    val parent_message: Any?,
    val reactions: Any?,
    val room: Int,
    val sender: SenderX,
    val status: Status?,
    val updated_at: String
)
