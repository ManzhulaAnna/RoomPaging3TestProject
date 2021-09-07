package com.test.domain.model

import java.util.*

data class Event(
    val id: String,
    val isFavorite: Boolean,
    val title: String,
    val date: Date,
    val url: String
)