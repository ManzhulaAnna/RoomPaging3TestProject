package com.test.domain

import androidx.annotation.WorkerThread
import com.test.data.repository.FavoriteRepository
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {

    @WorkerThread
    suspend operator fun invoke(id: String) {
        favoriteRepository.delete(id)
    }
}