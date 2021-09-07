package com.test.data.model

import androidx.room.Embedded
import androidx.room.Relation

class EventsAndFav {
    @Embedded
    lateinit var event: EventsDTO

    @Relation(
        parentColumn = EventsDTO.UID,
        entityColumn = FavoriteEventsDTO.UID,
        projection = [FavoriteEventsDTO.UID],
        entity = FavoriteEventsDTO::class
    )
    var favoriteUidKey: String? = null
}
