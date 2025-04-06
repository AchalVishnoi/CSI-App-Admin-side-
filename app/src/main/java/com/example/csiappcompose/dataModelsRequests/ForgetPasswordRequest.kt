package com.example.csiappcompose.dataModelsRequests

import android.os.Message

data class ForgotPasswordRequest(
    val email: String
)

data class ForgotPasswordResponse(
    val message: String?,
    val error: String?
)

data class resetPasswordRequest(
    val email: String,
    val otp: String,
    val password: String
)