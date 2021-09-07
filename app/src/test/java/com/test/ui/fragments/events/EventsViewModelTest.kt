package com.test.ui.fragments.events

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.test.App
import com.test.dispatchers.MockDispatchers
import com.test.domain.AddFavoriteUseCase
import com.test.domain.DeleteFavoriteUseCase
import com.test.domain.GetEventsUseCase
import com.test.domain.model.Event
import com.test.ui.fragments.events.adapter.EventsAdapter
import com.test.ui.fragments.events.interfaces.EventOnClickListener
import com.test.ui.fragments.events.mapper.EventsUiModelMapper
import com.test.ui.fragments.events.model.ErrorUiEvent
import com.test.ui.fragments.events.model.EventUiModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.Assert
import junit.framework.TestCase.assertEquals
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
class EventsViewModelTest {

    companion object {

        const val ERROR_MESSAGE = "error_message"
        const val ID = "1"
        const val TITLE = "name"
        const val URL = "url"
        const val DATE = "date"
        const val IS_IN_FAVORITE = true
        const val IS_NOT_IN_FAVORITE = false
    }

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @MockK
    lateinit var uiModelObserver: Observer<PagingData<EventUiModel>>

    @MockK
    lateinit var errorObserver: Observer<ErrorUiEvent>

    @MockK
    lateinit var urlClickObserver: Observer<String?>

    @MockK
    lateinit var getEventsUseCase: GetEventsUseCase

    @MockK
    lateinit var addFavoriteUseCase: AddFavoriteUseCase

    @MockK
    lateinit var deleteFavoriteUseCase: DeleteFavoriteUseCase

    private lateinit var eventsUiModelMapper: EventsUiModelMapper

    private lateinit var viewModel: EventsViewModel


    private val coroutineDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(coroutineDispatcher)
        eventsUiModelMapper = EventsUiModelMapper()
        viewModel = EventsViewModel(
            MockDispatchers(),
            getEventsUseCase,
            addFavoriteUseCase,
            deleteFavoriteUseCase,
            eventsUiModelMapper
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun WHEN_init_THEN_shouldGetEvents_AND_updateUiModel() {
        coroutineDispatcher.runBlockingTest {
            val eventList = listOf(Event(ID, IS_IN_FAVORITE, TITLE, Date(1000), URL))
            val pagingSource = PagingData.from(eventList)

            val captureUiModel = slot<PagingData<EventUiModel>>()
            every { uiModelObserver.onChanged(capture(captureUiModel)) } answers {}
            coEvery { getEventsUseCase.invoke() } returns flowOf(pagingSource)

            viewModel.uiModel.observeForever(uiModelObserver)

            val adapter = EventsAdapter(object : EventOnClickListener {})
            val job = launch {
                viewModel.uiModel.observeForever {
                    runBlocking {
                        adapter.submitData(it)
                    }
                }
            }

            val result = adapter.snapshot().items[0]
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

            every { getEventsUseCase.invoke() } answers { throw Exception(ERROR_MESSAGE) }

            viewModel.errorEvent.observeForever(errorObserver)
            viewModel.uiModel.observeForever(uiModelObserver)

            val captureErrorUiModel = slot<ErrorUiEvent>()
            verify { errorObserver.onChanged(capture(captureErrorUiModel)) }
            Assert.assertEquals(captureErrorUiModel.captured.error, ERROR_MESSAGE)

            viewModel.errorEvent.removeObserver(errorObserver)
            viewModel.uiModel.removeObserver(uiModelObserver)
        }
    }

    @Test
    fun WHEN_onOpenUrlClick_THEN_urlClickEvent_isSent() {
        runBlocking {

            val mock = mockk<EventUiModel>()
            every { mock.url } returns URL

            every { urlClickObserver.onChanged(any()) } answers {}

            viewModel.urlClickEvent.observeForever(urlClickObserver)

            viewModel.onOpenUrlClick(mock)

            val capture = slot<String>()
            verify { urlClickObserver.onChanged(capture(capture)) }
            Assert.assertEquals(capture.captured, URL)

            viewModel.urlClickEvent.removeObserver(urlClickObserver)
        }
    }

    @Test
    fun WHEN_onFavoriteClick_AND_eventIsInFavorite_SHOULD_beDeletedFromFavorites() {
        runBlocking {

            val mock = mockk<EventUiModel>()
            every { mock.id } returns ID
            every { mock.isFavorite } returns IS_IN_FAVORITE

            coEvery { deleteFavoriteUseCase.invoke(mock.id) } answers {}

            viewModel.onFavoriteClick(mock)

            coVerify { deleteFavoriteUseCase.invoke(ID) }

            viewModel.urlClickEvent.removeObserver(urlClickObserver)
        }
    }

    @Test
    fun WHEN_onFavoriteClick_AND_eventIs_NOT_InFavorite_SHOULD_beAddedToFavorites() {
        runBlocking {

            val mock = mockk<EventUiModel>()
            every { mock.id } returns ID
            every { mock.isFavorite } returns IS_NOT_IN_FAVORITE

            coEvery { addFavoriteUseCase.invoke(mock.id) } answers {}

            viewModel.onFavoriteClick(mock)

            coVerify { addFavoriteUseCase.invoke(ID) }

            viewModel.urlClickEvent.removeObserver(urlClickObserver)
        }
    }
}