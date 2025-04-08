package com.example.csiappcompose.dataModelsResponseTask

data class Group(
    val id: Int,
    val members: List<Member>,
    val name: String
)