package com.test.domain

import com.test.data.repository.FavoriteRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteFavoriteUseCaseTest{

    companion object {

        const val ID = "1"
    }

    @MockK
    lateinit var repository: FavoriteRepository

    private lateinit var useCase: DeleteFavoriteUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = DeleteFavoriteUseCase(repository)
    }

    @Test
    fun Should_callRepository_When_usecaseInvoked() = runBlocking {

        coEvery { repository.delete(ID) } answers {}

        useCase.invoke(ID)

        coVerify { repository.delete(ID) }
    }
}