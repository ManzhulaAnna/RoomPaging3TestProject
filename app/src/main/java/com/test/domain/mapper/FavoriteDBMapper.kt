package com.test.domain.mapper

import com.test.data.model.EventsDTO
import com.test.data.model.FavoriteEventsDTO
import javax.inject.Inject

class FavoriteDBMapper @Inject constructor() {
    fun map(event: EventsDTO): FavoriteEventsDTO {
        return FavoriteEventsDTO(
            id = event.id,
            eventName = event.title,
            date = event.eventDate,
            url = event.url,
        )
    }
}
