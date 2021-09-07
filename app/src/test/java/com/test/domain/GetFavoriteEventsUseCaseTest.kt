package com.test.domain

import androidx.paging.PagingData
import com.test.data.model.FavoriteEventsDTO
import com.test.data.repository.FavoriteRepository
import com.test.domain.mapper.FavoritesMapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

class GetFavoriteEventsUseCaseTest{

        companion object {

            const val ID = "1"
            const val TITLE = "name"
            const val URL = "url"
        }

        @MockK
        lateinit var repository: FavoriteRepository

        lateinit var mapper: FavoritesMapper

        private lateinit var useCase: GetFavoriteEventsUseCase

        @Before
        fun setUp() {
            MockKAnnotations.init(this)
            mapper = FavoritesMapper()
            useCase = GetFavoriteEventsUseCase(repository, mapper)
        }

        @Test
        fun Should_ReturnEvents_When_EventsExist() = runBlocking {

            val eventFavDto = FavoriteEventsDTO(ID, TITLE, Date(1000), URL)
            val eventList = listOf(eventFavDto)
            val pagingSource = PagingData.from(eventList)

            coEvery { repository.getFavorites() } returns flowOf(pagingSource)

            useCase.invoke()

            coVerify { repository.getFavorites() }
        }
    }