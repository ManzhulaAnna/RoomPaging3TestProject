package com.test.domain

import androidx.annotation.WorkerThread
import com.test.data.repository.EventsRepository
import com.test.data.repository.FavoriteRepository
import com.test.domain.mapper.FavoriteDBMapper
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val eventsRepository: EventsRepository,
    private val dbMapper: FavoriteDBMapper,
) {

    @WorkerThread
    suspend operator fun invoke(id: String) {
        eventsRepository.getEventById(id)?.let { event ->
            favoriteRepository.add(dbMapper.map(event))
        }
    }
}