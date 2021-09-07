package com.test.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events_keys")
data class EventsKeyDTO(
    @PrimaryKey
    @ColumnInfo(name = UID)
    @NonNull val id: String,

    @ColumnInfo(name = PREV_KEY)
    val prevKey: Int?,

    @ColumnInfo(name = NEXT_KEY)
    val nextKey: Int?
) {
    companion object {
        private const val UID = "uid"
        private const val PREV_KEY = "prev_key"
        private const val NEXT_KEY = "next_key"
    }
}