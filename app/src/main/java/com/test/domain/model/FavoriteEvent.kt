package com.test.domain.model

import java.util.*

data class FavoriteEvent(
    val id: String,
    val title: String,
    val date: Date,
    val url: String
)