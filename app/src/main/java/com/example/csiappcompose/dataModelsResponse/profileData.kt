package com.example.csiappcompose.dataModelsResponse

data class profileData(
    val achievements: String,
    val bio: String,
    val branch: String,
    val dob: String,
    val domain: DomainX,
    val full_name: String,
    val github_url: String,
    val linkedin_url: String,
    val photo: String?,
    val year: String
)