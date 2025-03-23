package com.example.csiappcompose.dataModelsResponse

data class searchMemberItem(
    val first_name: String,
    val id: Int,
    val last_name: String,
    val photo: Any,
    val role: String,
    val year: String
)