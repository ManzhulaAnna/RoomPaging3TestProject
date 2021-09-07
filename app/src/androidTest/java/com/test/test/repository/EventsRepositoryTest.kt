package com.test.test.repository

import android.app.Application
import android.content.Context
import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.test.BuildConfig
import com.test.api.MockApi
import com.test.data.database.EventsDb
import com.test.data.local.DataStatePrefs
import com.test.data.mappers.EventsItemValidator
import com.test.data.mappers.EventsRawMapper
import com.test.data.mappers.FavoriteDTOMapper
import com.test.data.model.EventsAndFav
import com.test.data.model.EventsDTO
import com.test.data.repository.EventsRemoteMediator
import com.test.data.repository.EventsRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.easymock.EasyMock
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class EventsRepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    companion object {
        const val ID = "5349612"
    }

    private lateinit var repository: EventsRepository
    private lateinit var mediator: EventsRemoteMediator

    @Inject
    lateinit var mockApi: MockApi

    private val eventExpected = EventsDTO(
        internalId = 1,
        id = ID,
        title = "TEST Item 1",
        url = "https://seatgeek.com/us-open-tennis-grounds-admission-september-4-tickets/tennis/2021-09-04-11-am/5349612",
        eventDate = Date(1630767600000)
    )

    private val pagingState = PagingState<Int, EventsAndFav>(
        listOf(),
        anchorPosition = null,
        PagingConfig(5),
        leadingPlaceholderCount = 5,
    )

    @Before
    fun setup() {
        hiltRule.inject()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val eventsDb = Room.inMemoryDatabaseBuilder(context, EventsDb::class.java).build()
        val dbState = DataStatePrefs(context.applicationContext as Application)
        val mapper = EventsRawMapper(EventsItemValidator())
        val favoriteMapper = FavoriteDTOMapper()

        mediator = EventsRemoteMediator(
            eventsDb,
            eventsDb.events(),
            eventsDb.favorites(),
            eventsDb.eventsKey(),
            mockApi,
            dbState,
            mapper,
            favoriteMapper,
            BuildConfig.CLIENT_ID,
            false
        )
        val mockFactory: EventsRemoteMediator.Factory =
            EasyMock.createMock(EventsRemoteMediator.Factory::class.java)
        EasyMock.expect(mockFactory.create(BuildConfig.CLIENT_ID)).andReturn(mediator)
        EasyMock.replay(mockFactory)
        repository = EventsRepository(mockFactory, eventsDb.events())
    }

    @Test
    fun WHEN_loadCalled_THEN_shouldInsertInEventsDao() {
        runBlocking {

            //WHEN
            mediator.load(LoadType.REFRESH, pagingState)
            val result = repository.getEventById(ID)

            // THEN
            assertThat(result, equalTo(eventExpected))
        }
    }

    @Test
    @OptIn(ExperimentalPagingApi::class)
    fun WHEN_refreshLoad_THEN_ReturnsSuccessResult_WhenMoreDataIsPresent() {
        runBlocking {
            //WHEN
            val result = mediator.load(LoadType.REFRESH, pagingState)

            //THEN
            assertTrue(result is RemoteMediator.MediatorResult.Success)
            assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        }
    }
}