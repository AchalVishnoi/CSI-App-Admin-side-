package com.example.csiappcompose.dataModelsResponse

import java.util.UUID

data class oldChatMessage(
    val localId: String = UUID.randomUUID().toString(),
    val attachment: Any?,
    var content: String,
    val created_at: String?,
    val id: Int?,
    val is_deleted: Boolean?,
    val is_edited: Boolean?,
    val mentions: List<Int?>?,
    val message_type: String?,
    val parent_message: Any?,
    var reactions: Reactions?,
    val room: Int?,
    val sender: SenderX,
    val status: Status?,
    val updated_at: String?,
    val is_self: Boolean,
    val sendingStatus: String?,
    val new_content: String?,
    val action: String?,
    var is_typing: Boolean?
)
