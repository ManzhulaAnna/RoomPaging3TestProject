package com.test.domain.mapper

import com.test.data.model.EventsDTO
import com.test.domain.model.Event
import javax.inject.Inject

class EventDBMapper @Inject constructor() {
    fun map(event: Event): EventsDTO {
        return EventsDTO(
            id = event.id,
            title = event.title,
            eventDate = event.date,
            url = event.url,
        )
    }
}
