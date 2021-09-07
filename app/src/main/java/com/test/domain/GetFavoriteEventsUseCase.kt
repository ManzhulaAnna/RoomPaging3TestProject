package com.test.domain

import com.test.data.repository.FavoriteRepository
import com.test.domain.mapper.FavoritesMapper
import javax.inject.Inject

class GetFavoriteEventsUseCase @Inject constructor(
    private val repository: FavoriteRepository,
    private val mapper: FavoritesMapper,
) {

    operator fun invoke() = mapper.map(repository.getFavorites())
}