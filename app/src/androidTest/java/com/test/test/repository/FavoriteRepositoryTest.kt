package com.test.test.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.test.data.database.EventsDb
import com.test.data.database.dao.FavoritesDao
import com.test.data.model.FavoriteEventsDTO
import com.test.data.repository.FavoriteRepository
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class FavoriteRepositoryTest {

    companion object {

        const val ID = "1"
        const val ANOTHER_ITEM_ID = "2"
        const val TITLE = "name"
        const val URL = "url"
        const val IS_FAVORITE = 1
        const val IS_NOT_FAVORITE = 0
    }

    private lateinit var repository: FavoriteRepository
    private lateinit var favoritesDao: FavoritesDao
    private lateinit var eventsDb: EventsDb

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        eventsDb = Room.inMemoryDatabaseBuilder(context, EventsDb::class.java).build()
        favoritesDao = eventsDb.favorites()
        repository = FavoriteRepository(eventsDb, favoritesDao)
    }

    @Test
    @Throws(Exception::class)
    fun WHEN_addCalled_THEN_shouldInsertInFavoritesDao() {
        runBlocking {

            // GIVEN
            val eventFavDto = FavoriteEventsDTO(ID, TITLE, Date(1000), URL)

            // WHEN
            repository.add(eventFavDto)

            // THEN
            val result = favoritesDao.isFavorite(ID)
            assertThat(result, equalTo(IS_FAVORITE))
        }
    }

    @Test
    fun WHEN_deleteByIdCalled_THEN_shouldDeleteByIdInFavoritesDao() {
        runBlocking {
            // GIVEN
            val eventFavDto = FavoriteEventsDTO(ID, TITLE, Date(1000), URL)

            // WHEN
            repository.add(eventFavDto)
            repository.delete(ID)

            // THEN
            val result = favoritesDao.isFavorite(ID)
            assertThat(result, equalTo(IS_NOT_FAVORITE))
        }
    }

    @Test
    fun WHEN_getFavorites_THEN_shouldDeleteByIdInFavoritesDao() {
        runBlocking {

            // GIVEN
            val eventFavDto = FavoriteEventsDTO(ID, TITLE, Date(1000), URL)
            val anotherEventFavDto = FavoriteEventsDTO(ANOTHER_ITEM_ID, TITLE, Date(1000), URL)

            // WHEN
            repository.add(eventFavDto)
            repository.add(anotherEventFavDto)

            repository.delete(ID)

            // THEN
            val isFirstItemFavorite = favoritesDao.isFavorite(ID)
            val isAnotherItemFavorite = favoritesDao.isFavorite(ANOTHER_ITEM_ID)

            assertThat(isFirstItemFavorite, equalTo(IS_NOT_FAVORITE))
            assertThat(isAnotherItemFavorite, equalTo(IS_FAVORITE))
        }
    }
}