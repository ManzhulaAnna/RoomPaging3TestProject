package com.test.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.test.BuildConfig
import com.test.data.database.EventsDb
import com.test.data.database.dao.EventsDao
import com.test.data.database.dao.EventsKeysDao
import com.test.data.database.dao.FavoritesDao
import com.test.data.local.DataStatePrefs
import com.test.data.mappers.EventsRawMapper
import com.test.data.mappers.FavoriteDTOMapper
import com.test.data.misk.extension.isEventChanged
import com.test.data.model.EventsAndFav
import com.test.data.model.EventsDTO
import com.test.data.model.EventsKeyDTO
import com.test.data.model.FavoriteEventsDTO
import com.test.data.network.EventsService
import com.test.data.network.EventsService.Companion.INITIAL_PAGE
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class EventsRemoteMediator @AssistedInject constructor(
    private val eventsDB: EventsDb,
    private val eventsDao: EventsDao,
    private val favoritesDao: FavoritesDao,
    private val eventsKeyDao: EventsKeysDao,
    private val eventsService: EventsService,
    private val dbState: DataStatePrefs,
    private val mapper: EventsRawMapper,
    private val favoriteMapper: FavoriteDTOMapper,
    @Assisted(ASSISTED_CLIENT_ID) private val clientId: String,
    @Assisted(IS_FAV_CAN_BE_CHANGED) private val isUpdateFav: Boolean,
) : RemoteMediator<Int, EventsAndFav>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.HOURS.toMillis(BuildConfig.REMOTE_CACHE_TIMEOUT)
        return if (dbState.lastUpdate > 0 && getNow() - dbState.lastUpdate <= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            return InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EventsAndFav>
    ): MediatorResult {
        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> return pageKeyData
            is Int -> pageKeyData
            else -> INITIAL_PAGE
        }

        try {
            val data = eventsService.getEvents(
                clientId = clientId,
                page = page,
                pageSize = state.config.pageSize,
            )

            val items: List<EventsDTO>?
            if (data.isSuccessful) {
                items = mapper.toEventsDTOList(data.body()?.events)
            } else {
                return MediatorResult.Error(HttpException(data))
            }

            updateDataBase(loadType, page, items)

            return MediatorResult.Success(endOfPaginationReached = items.isNullOrEmpty())
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun updateDataBase(
        loadType: LoadType,
        page: Int,
        items: List<EventsDTO>?
    ) {
        eventsDB.withTransaction {
            if (loadType == LoadType.REFRESH) {
                dbState.lastUpdate = getNow() // Refresh cache timestamp

                eventsDao.deleteData()
                eventsKeyDao.deleteAll()
            }

            val prevKey = if (page == INITIAL_PAGE) null else (page - 1)
            val nextKey = if (items.isNullOrEmpty()) null else (page + 1)
            val keys = items?.map {
                EventsKeyDTO(
                    id = it.id,
                    prevKey = prevKey,
                    nextKey = nextKey,
                )
            }
            keys?.let { eventsKeyDao.insertAll(it) }
            items?.let { eventsDao.insertAll(it) }

            updateFavorites(items)
        }
    }

    private suspend fun updateFavorites(items: List<EventsDTO>?) {
        if (!isUpdateFav || items.isNullOrEmpty()) {
            return
        }

        val ids = items.map { it.id }
        val added = favoritesDao.getByIds(ids)
        if (added.isNullOrEmpty()) {
            return
        }

        val listToAdd = mutableListOf<FavoriteEventsDTO>()
        added.forEach { addedItem ->
            items.firstOrNull { it.id == addedItem.id }?.let { networkEvent ->
                if (addedItem.isEventChanged(networkEvent)) {
                    listToAdd.add(favoriteMapper.map(networkEvent))
                }
            }
        }
        if (listToAdd.isEmpty()) {
            return
        }
        eventsDB.withTransaction {
            favoritesDao.insertAll(listToAdd)
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, EventsAndFav>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestToCurrent(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey
                    ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = false)
                prevKey
            }
        }
    }

    private suspend fun getClosestToCurrent(state: PagingState<Int, EventsAndFav>): EventsKeyDTO? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.event?.id?.let { id ->
                eventsKeyDao.remoteKeyById(id)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, EventsAndFav>): EventsKeyDTO? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.event?.id?.let { id -> eventsKeyDao.remoteKeyById(id) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, EventsAndFav>): EventsKeyDTO? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.event?.id?.let { id -> eventsKeyDao.remoteKeyById(id) }
    }

    private fun getNow() = System.currentTimeMillis()

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(ASSISTED_CLIENT_ID) clientId: String,
            @Assisted(IS_FAV_CAN_BE_CHANGED) isUpdateFav: Boolean = true,
        ): EventsRemoteMediator
    }

    companion object {
        private const val ASSISTED_CLIENT_ID = "clientId"
        private const val IS_FAV_CAN_BE_CHANGED = "favChange"
    }
}