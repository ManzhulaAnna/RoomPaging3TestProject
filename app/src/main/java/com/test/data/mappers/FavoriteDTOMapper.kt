package com.test.data.mappers

import com.test.data.model.EventsDTO
import com.test.data.model.FavoriteEventsDTO
import javax.inject.Inject

class FavoriteDTOMapper @Inject constructor() {
    fun map(event: EventsDTO): FavoriteEventsDTO {
        return FavoriteEventsDTO(
            id = event.id,
            eventName = event.title,
            date = event.eventDate,
            url = event.url,
        )
    }
}
