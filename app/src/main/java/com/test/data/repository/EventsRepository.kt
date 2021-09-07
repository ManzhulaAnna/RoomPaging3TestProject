package com.test.data.repository

import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import com.test.BuildConfig
import com.test.data.database.dao.EventsDao
import com.test.data.model.EventsDTO
import com.test.data.repository.settings.PagingConfigSettings
import javax.inject.Inject

class EventsRepository @Inject constructor(
    private val remoteMediator: EventsRemoteMediator.Factory,
    private val eventsDao: EventsDao,
) {

    @WorkerThread
    suspend fun getEventById(id: String): EventsDTO? {
        return eventsDao.getById(id)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getEvents() = Pager(
        config = PagingConfigSettings.create(),
        remoteMediator = remoteMediator.create(BuildConfig.CLIENT_ID),
    ) { eventsDao.getEventsAndFav() }
        .flow
}