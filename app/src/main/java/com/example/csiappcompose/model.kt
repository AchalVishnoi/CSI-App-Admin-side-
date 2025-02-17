package com.example.csiappcompose

import java.io.Serializable

data class CurrentTask(
    val data: String,
    val name: String,
    val progress: Int,
): Serializable

data class PendingTask(
    val data: String,
    val name: String,
    val progress: Int,
): Serializable

data class PreviousTask(
    val data: String,
    val name: String,
    val progress: Int,
): Serializable