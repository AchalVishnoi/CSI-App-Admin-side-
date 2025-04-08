package com.example.csiappcompose.dataModelsResponseTask

data class Current(
    val attachment: Any,
    val current_progress: Int,
    val description: String,
    val end_date: String,
    val groups: List<Group>,
    val id: Int,
    val info_url: String,
    val start_date: String,
    val status: String,
    val title: String
)