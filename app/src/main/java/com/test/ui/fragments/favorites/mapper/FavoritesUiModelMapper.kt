package com.test.ui.fragments.favorites.mapper

import androidx.paging.PagingData
import androidx.paging.map
import com.test.domain.model.FavoriteEvent
import com.test.ui.fragments.favorites.model.FavoriteEventUiModel
import com.test.ui.misk.formatToUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesUiModelMapper @Inject constructor() {

    fun map(flow: Flow<PagingData<FavoriteEvent>>): Flow<PagingData<FavoriteEventUiModel>> {
        return flow.map {
            it.map { event ->
                FavoriteEventUiModel(
                    id = event.id,
                    title = event.title,
                    url = event.url,
                    date = event.date.formatToUI(),
                )
            }
        }
    }
}