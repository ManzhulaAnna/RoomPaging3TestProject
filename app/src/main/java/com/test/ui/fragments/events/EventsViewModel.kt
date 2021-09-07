package com.test.ui.fragments.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.test.dispatcher.DispatchersProvider
import com.test.domain.AddFavoriteUseCase
import com.test.domain.DeleteFavoriteUseCase
import com.test.domain.GetEventsUseCase
import com.test.ui.fragments.events.interfaces.EventOnClickListener
import com.test.ui.fragments.events.mapper.EventsUiModelMapper
import com.test.ui.fragments.events.model.ErrorUiEvent
import com.test.ui.fragments.events.model.EventUiModel
import com.test.ui.misk.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    dispatchers: DispatchersProvider,
    private val getGetEventsUseCase: GetEventsUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    private val eventsUiModelMapper: EventsUiModelMapper,
) : ViewModel(),
    EventOnClickListener {

    private val coroutineIoScope = CoroutineScope(dispatchers.io())

    val uiModel: LiveData<PagingData<EventUiModel>> by lazy {
        MutableLiveData<PagingData<EventUiModel>>().also {
            init(it)
        }
    }

    private val _urlClickEvent: SingleLiveEvent<String?> = SingleLiveEvent()
    val urlClickEvent: LiveData<String?>
        get() = _urlClickEvent

    private val _errorEvent: MutableLiveData<ErrorUiEvent> = MutableLiveData()
    val errorEvent: LiveData<ErrorUiEvent>
        get() = _errorEvent

    override fun onFavoriteClick(event: EventUiModel) {
        coroutineIoScope.launch {
            if (event.isFavorite) {
                deleteFavoriteUseCase(event.id)
            } else {
                addFavoriteUseCase(event.id)
            }
        }
    }

    override fun onOpenUrlClick(event: EventUiModel) {
        _urlClickEvent.setValue(event.url)
    }

    private fun init(initialUiModel: MutableLiveData<PagingData<EventUiModel>>) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, error ->
                handleError(error)
                Timber.e(error)
            }
        ) {
            val events = getGetEventsUseCase()
            val eventUiModels = eventsUiModelMapper.map(events)
            eventUiModels.cachedIn(viewModelScope)
                .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
                .collectLatest { initialUiModel.postValue(it) }
        }
    }

    private fun handleError(error: Throwable) {
        error.message?.let {
            _errorEvent.postValue(ErrorUiEvent(it))
        }
    }
}