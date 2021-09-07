package com.test.data.repository

import androidx.annotation.WorkerThread
import androidx.paging.Pager
import androidx.room.withTransaction
import com.test.data.database.EventsDb
import com.test.data.database.dao.FavoritesDao
import com.test.data.model.FavoriteEventsDTO
import com.test.data.repository.settings.PagingConfigSettings
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val eventsDB: EventsDb,
    private val favoritesDao: FavoritesDao,
) {

    @WorkerThread
    suspend fun add(item: FavoriteEventsDTO) {
        eventsDB.withTransaction {
            favoritesDao.insert(item)
        }
    }

    @WorkerThread
    suspend fun delete(id: String) {
        eventsDB.withTransaction {
            favoritesDao.deleteById(id)
        }
    }

    fun getFavorites() = Pager(
        config = PagingConfigSettings.create(),
        pagingSourceFactory = { eventsDB.favorites().getPagedSource() }
    ).flow
}