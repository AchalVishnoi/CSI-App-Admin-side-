package com.example.csiappcompose.dataModelsResponse

data class HomePageStats(
    val announcement_count: Int,
    val chat_groups_with_unread: Int,
    val name: String,
    val tasks_assigned: Int,
    val photo: String?,
    val year:String
)