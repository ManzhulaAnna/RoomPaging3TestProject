package com.test.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "events_cache",
    indices = [
        Index(value = [EventsDTO.UID], unique = true),
        Index(value = [EventsDTO.EVENT_DATE], unique = false),
    ]
)
data class EventsDTO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = INTERNAL_ID)
    @NonNull val internalId: Long = 0,

    @ColumnInfo(name = UID)
    @NonNull val id: String,

    @ColumnInfo(name = EVENT_NAME)
    @NonNull val title: String,

    @ColumnInfo(name = EVENT_DATE)
    @NonNull val eventDate: Date,

    @NonNull val url: String,
) {

    companion object {
        const val UID = "uid"
        const val INTERNAL_ID = "internal_id"
        const val EVENT_NAME = "event_name"
        const val EVENT_DATE = "event_date"
    }
}