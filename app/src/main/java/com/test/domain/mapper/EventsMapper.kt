package com.test.domain.mapper

import androidx.paging.PagingData
import androidx.paging.map
import com.test.data.model.EventsAndFav
import com.test.domain.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventsMapper @Inject constructor() {

    fun map(flow: Flow<PagingData<EventsAndFav>>): Flow<PagingData<Event>> {
        return flow.map {
            it.map { eventAndFav ->
                Event(
                    id = eventAndFav.event.id,
                    title = eventAndFav.event.title,
                    date = eventAndFav.event.eventDate,
                    url = eventAndFav.event.url,
                    isFavorite = isFavorite(eventAndFav.favoriteUidKey)
                )
            }
        }
    }

    private fun isFavorite(favoriteUid: String?): Boolean {
        return !favoriteUid.isNullOrEmpty()
    }
}