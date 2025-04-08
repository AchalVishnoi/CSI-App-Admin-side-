package com.example.csiappcompose.dataModelsResponseTask

data class TaskData(
    val completed: List<Completed>,
    val current: List<Current>,
    val pending: List<Pending>
)