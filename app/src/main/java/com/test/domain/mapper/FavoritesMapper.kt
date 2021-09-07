package com.test.domain.mapper

import androidx.paging.PagingData
import androidx.paging.map
import com.test.data.model.FavoriteEventsDTO
import com.test.domain.model.FavoriteEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesMapper @Inject constructor() {

    fun map(flow: Flow<PagingData<FavoriteEventsDTO>>): Flow<PagingData<FavoriteEvent>> {
        return flow.map {
            it.map { event ->
                FavoriteEvent(
                    id = event.id,
                    title = event.eventName,
                    date = event.date,
                    url = event.url
                )
            }
        }
    }
}