package com.example.csiappcompose.dataModelsResponse

data class LoginResponse(
    val message: String,
    val token: String,
    val is_completed: Boolean
)