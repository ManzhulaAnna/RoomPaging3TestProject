package com.test.domain

import androidx.paging.PagingData
import com.test.data.model.EventsAndFav
import com.test.data.model.EventsDTO
import com.test.data.repository.EventsRepository
import com.test.domain.mapper.EventsMapper
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

class GetEventsUseCaseTest {

    companion object {

        const val ID = "1"
        const val TITLE = "name"
        const val URL = "url"
    }

    @MockK
    lateinit var repository: EventsRepository

    lateinit var mapper: EventsMapper

    private lateinit var useCase: GetEventsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mapper = EventsMapper()
        useCase = GetEventsUseCase(repository, mapper)
    }

    @Test
    fun Should_ReturnEvents_When_EventsExist() = runBlocking {
        val eventDto = EventsDTO(0, ID, TITLE, Date(1000), URL)
        val favoriteUidKey = ID
        val eventsAndFav = mockk<EventsAndFav>()
        every { eventsAndFav.event } returns eventDto
        every { eventsAndFav.favoriteUidKey } returns favoriteUidKey
        val eventList = listOf(EventsAndFav())
        val pagingSource = PagingData.from(eventList)

        coEvery { repository.getEvents() } returns flowOf(pagingSource)

        useCase.invoke()

        coVerify { repository.getEvents() }
    }
}