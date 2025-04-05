package com.example.csiappcompose.dataModelsResponse

data class Data(
    val amount: String,
    val created_at: String,
    val description: String,
    val event_date: String,
    val gallery_files: List<String>,
    val guidelines: String,
    val id: Int,
    val is_registrations_open: Boolean,
    val payment_required: Boolean,
    val poster: String,
    val registration_end_date: String,
    val registration_start_date: String,
    val status: String,
    val title: String,
    val updated_at: String,
    val venue: String
)