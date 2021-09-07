package com.test.ui.fragments.favorites

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.test.App
import com.test.dispatchers.MockDispatchers
import com.test.domain.DeleteFavoriteUseCase
import com.test.domain.GetFavoriteEventsUseCase
import com.test.domain.model.FavoriteEvent
import com.test.ui.fragments.favorites.callback.DiffFavoriteEventCallback
import com.test.ui.fragments.favorites.callback.NoopListCallback
import com.test.ui.fragments.favorites.mapper.FavoritesUiModelMapper
import com.test.ui.fragments.favorites.model.ErrorUiFavorite
import com.test.ui.fragments.favorites.model.FavoriteEventUiModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.LOLLIPOP, Build.VERSION_CODES.Q], application = App::class)
class FavoritesViewModelTest {

    companion object {
        const val ERROR_MESSAGE = "error_message"
        const val ID = "1"
        const val TITLE = "name"
        const val URL = "url"
        const val DATE = "date"
    }

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @MockK
    lateinit var uiModelObserver: Observer<PagingData<FavoriteEventUiModel>>

    @MockK
    lateinit var errorObserver: Observer<ErrorUiFavorite>

    @MockK
    lateinit var urlClickObserver: Observer<String?>

    @MockK
    lateinit var getFavoriteUseCase: GetFavoriteEventsUseCase

    @MockK
    lateinit var deleteFavoriteUseCase: DeleteFavoriteUseCase


    lateinit var favoritesUiModelMapper: FavoritesUiModelMapper

    private lateinit var viewModel: FavoritesViewModel


    private val coroutineDispatcher = TestCoroutineDispatcher()
    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
        favoritesUiModelMapper = FavoritesUiModelMapper()
        viewModel = FavoritesViewModel(
            MockDispatchers(),
            getFavoriteUseCase,
            deleteFavoriteUseCase,
            favoritesUiModelMapper
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun WHEN_init_THEN_shouldGetEvents_AND_updateUiModel() {
        coroutineDispatcher.runBlockingTest {
            val eventList = listOf(FavoriteEvent(ID, TITLE, Date(1000), URL))
            val pagingSource = PagingData.from(eventList)

            val captureUiModel = slot<PagingData<FavoriteEventUiModel>>()
            every { uiModelObserver.onChanged(capture(captureUiModel)) } answers {}
            coEvery { getFavoriteUseCase.invoke() } returns flowOf(pagingSource)

            viewModel.uiModel.observeForever(uiModelObserver)

            val differ = AsyncPagingDataDiffer(
                diffCallback = DiffFavoriteEventCallback(),
                updateCallback = NoopListCallback(),
                workerDispatcher = Dispatchers.Main
            )

            val job = launch {
                viewModel.uiModel.observeForever {
                    runBlocking {
                        differ.submitData(it)
                    }
                }
            }

            val result = differ.snapshot().items[0]
            assertEquals(result.id, ID)
            assertEquals(result.title, TITLE)
            assertEquals(result.url, URL)

            job.cancel()

            viewModel.uiModel.removeObserver(uiModelObserver)
        }
    }

    @Test
    fun WHEN_init_AND_fails_THEN_shouldDisplayErrorMessage() {
        runBlocking {

            every { errorObserver.onChanged(any()) } answers {}
            every { uiModelObserver.onChanged(any()) } answers {}

            every { getFavoriteUseCase.invoke() } answers { throw Exception(ERROR_MESSAGE) }

            viewModel.errorEvent.observeForever(errorObserver)
            viewModel.uiModel.observeForever(uiModelObserver)

            val captureErrorUiModel = slot<ErrorUiFavorite>()
            verify { errorObserver.onChanged(capture(captureErrorUiModel)) }
            assertEquals(captureErrorUiModel.captured.error, ERROR_MESSAGE)

            viewModel.errorEvent.removeObserver(errorObserver)
            viewModel.uiModel.removeObserver(uiModelObserver)
        }
    }

    @Test
    fun WHEN_onOpenUrlClick_THEN_urlClickEvent_isSent() {
        runBlocking {

            val mock = mockk<FavoriteEventUiModel>()
            every { mock.url } returns URL

            every { urlClickObserver.onChanged(any()) } answers {}

            viewModel.urlClickEvent.observeForever(urlClickObserver)

            viewModel.onOpenUrlClick(mock)

            val capture = slot<String>()
            verify { urlClickObserver.onChanged(capture(capture)) }
            assertEquals(capture.captured, URL)

            viewModel.urlClickEvent.removeObserver(urlClickObserver)
        }
    }

    @Test
    fun WHEN_onFavoriteClick_THEN_eventShouldBeDeletedFromFavorites() {
        runBlocking {

            val mock = mockk<FavoriteEventUiModel>()
            every { mock.id } returns ID

            coEvery { deleteFavoriteUseCase.invoke(mock.id) } answers {}

            viewModel.onFavoriteClick(mock)

            coVerify { deleteFavoriteUseCase.invoke(ID) }

            viewModel.urlClickEvent.removeObserver(urlClickObserver)
        }
    }
}