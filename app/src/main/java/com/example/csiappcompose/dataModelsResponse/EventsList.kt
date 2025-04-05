package com.example.csiappcompose.dataModelsResponse

data class EventsList(
    val ongoing_events: List<Event>
)

data class Event(
    val id: Int,
    val poster: String
)