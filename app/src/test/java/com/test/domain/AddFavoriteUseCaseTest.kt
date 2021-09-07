package com.test.domain

import com.test.data.model.EventsDTO
import com.test.data.model.FavoriteEventsDTO
import com.test.data.repository.EventsRepository
import com.test.data.repository.FavoriteRepository
import com.test.domain.mapper.FavoriteDBMapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

class AddFavoriteUseCaseTest{

    companion object {

        const val ID = "1"
        const val TITLE = "name"
        const val URL = "url"
    }

    @MockK
    lateinit var repository: FavoriteRepository

    @MockK
    lateinit var eventsRepository: EventsRepository

    lateinit var mapper: FavoriteDBMapper

    private lateinit var useCase: AddFavoriteUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mapper = FavoriteDBMapper()
        useCase = AddFavoriteUseCase(repository, eventsRepository, mapper)
    }

    @Test
    fun Should_callRepository_When_usecaseInvoked() = runBlocking {

        val eventDto = EventsDTO(0,
            ID, TITLE, Date(1000), URL
        )

        val favoritesDato = FavoriteEventsDTO(
            id = ID,
            eventName = TITLE,
            date = Date(1000),
            url = URL
        )

        coEvery { repository.add(any()) } answers {}
        coEvery { eventsRepository.getEventById(ID) } answers { eventDto }

        useCase.invoke(ID)

        coVerify { repository.add(favoritesDato) }
        coVerify { eventsRepository.getEventById(ID) }
    }
}