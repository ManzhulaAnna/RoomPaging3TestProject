package com.test.data.misk.extension

import com.test.data.model.EventsDTO
import com.test.data.model.FavoriteEventsDTO

fun FavoriteEventsDTO.isEventChanged(events: EventsDTO): Boolean {
    return events.id != id
            || events.eventDate != date
            || events.title != eventName
            || events.url != url
}