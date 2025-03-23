package com.example.csiappcompose.dataModelsResponse

data class GroupListItem(
    val created_at: String,
    val description: String,
    val id: Int,
    val members_count: Int,
    val name: String,
    val room_avatar: Any,
    val updated_at: String,
    val last_message: oldChatMessage
)