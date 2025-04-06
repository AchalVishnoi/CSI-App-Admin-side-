package com.example.csiappcompose.dataModelsResponse

import java.util.UUID

data class chatMessages(

    val attachment: Any,
    val created_at: String,
    val id: Int?,
    var message: String,
    val message_type: String?,
    val room: Int?,
    val sender: Sender,
    val is_self: Boolean,
    val action: String?,
    var reactions: Reactions?,
    val new_content: String?,
    var is_typing: Boolean?
)
