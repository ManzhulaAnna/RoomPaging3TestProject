package com.test.data.model

import com.google.gson.annotations.SerializedName

data class EventsRaw(
    val events: List<EventRawItem>? = null,
    val meta: MetaRaw? = null,
)

data class EventRawItem(
    val id: String? = null,
    val url: String? = null,
    val title: String? = null,
    @SerializedName("datetime_utc")
    val utsTime: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
)

data class MetaRaw(
    val total: String? = null,
)