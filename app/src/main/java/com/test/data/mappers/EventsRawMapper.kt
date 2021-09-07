package com.test.data.mappers

import com.test.data.model.EventRawItem
import com.test.data.model.EventsDTO
import com.test.data.misk.extension.parseDate
import javax.inject.Inject

class EventsRawMapper @Inject constructor(
    private val validator: EventsItemValidator,
) {

    fun toEventsDTOList(events: List<EventRawItem>? = null): List<EventsDTO>? {
        return events
            ?.filter { item -> validator.isValid(item) }
            ?.mapNotNull { item -> toEventsDTOItem(item) }
    }

    private fun toEventsDTOItem(item: EventRawItem): EventsDTO? {
        val parsedDate = item.utsTime.parseDate()
        return if (item.id != null && item.title != null && parsedDate != null && item.url != null) {
            EventsDTO(
                id = item.id,
                title = item.title,
                eventDate = parsedDate,
                url = item.url,
            )
        } else {
            null
        }
    }
}