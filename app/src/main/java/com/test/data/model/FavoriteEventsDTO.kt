package com.test.data.model

import androidx.annotation.NonNull
import androidx.room.*
import java.util.*

@Entity(
    tableName = "events_favorite",
    indices = [Index(value = [FavoriteEventsDTO.EVENT_DATE], unique = false)]
)
data class FavoriteEventsDTO(
    @PrimaryKey
    @ColumnInfo(name = UID)
    @NonNull val id: String,

    @ColumnInfo(name = EVENT_NAME)
    @NonNull val eventName: String,

    @ColumnInfo(name = EVENT_DATE)
    @NonNull val date: Date,

    @NonNull val url: String,
) {
    companion object {
        const val UID = "uid"
        const val EVENT_NAME = "event_name"
        const val EVENT_DATE = "date"
    }
}
