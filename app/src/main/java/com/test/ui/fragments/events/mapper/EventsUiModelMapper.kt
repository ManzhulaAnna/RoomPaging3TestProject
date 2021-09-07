package com.test.ui.fragments.events.mapper

import androidx.paging.PagingData
import androidx.paging.map
import com.test.domain.model.Event
import com.test.ui.fragments.events.model.EventUiModel
import com.test.ui.misk.formatToUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventsUiModelMapper @Inject constructor() {

    fun map(flow: Flow<PagingData<Event>>): Flow<PagingData<EventUiModel>> {
        return flow.map {
            it.map { event ->
                EventUiModel(
                    id = event.id,
                    title = event.title,
                    url = event.url,
                    date = event.date.formatToUI(),
                    isFavorite = event.isFavorite
                )
            }
        }
    }
}