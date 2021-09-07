package com.test.ui.fragments.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.test.dispatcher.DispatchersProvider
import com.test.domain.DeleteFavoriteUseCase
import com.test.domain.GetFavoriteEventsUseCase
import com.test.ui.fragments.favorites.interfaces.FavoritesOnClickListener
import com.test.ui.fragments.favorites.mapper.FavoritesUiModelMapper
import com.test.ui.fragments.favorites.model.ErrorUiFavorite
import com.test.ui.fragments.favorites.model.FavoriteEventUiModel
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
class FavoritesViewModel @Inject constructor(
    dispatchers: DispatchersProvider,
    private val getGetFavoriteUseCase: GetFavoriteEventsUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    private val favoritesUiModelMapper: FavoritesUiModelMapper,
) : ViewModel(),
    FavoritesOnClickListener {

    private val coroutineIoScope = CoroutineScope(dispatchers.io())

    val uiModel: LiveData<PagingData<FavoriteEventUiModel>> by lazy {
        MutableLiveData<PagingData<FavoriteEventUiModel>>().also {
            init(it)
        }
    }

    private val _urlClickEvent: SingleLiveEvent<String?> = SingleLiveEvent()
    val urlClickEvent: LiveData<String?>
        get() = _urlClickEvent

    private val _errorEvent: MutableLiveData<ErrorUiFavorite> = MutableLiveData()
    val errorEvent: LiveData<ErrorUiFavorite>
        get() = _errorEvent

    override fun onFavoriteClick(event: FavoriteEventUiModel) {
        coroutineIoScope.launch {
            deleteFavoriteUseCase(event.id)
        }
    }

    override fun onOpenUrlClick(event: FavoriteEventUiModel) {
        _urlClickEvent.setValue(event.url)
    }

    private fun init(initialUiModel: MutableLiveData<PagingData<FavoriteEventUiModel>>) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, error ->
                handleError(error)
                Timber.e(error)
            }
        ) {
            val events = getGetFavoriteUseCase()
            val eventUiModels = favoritesUiModelMapper.map(events)
            eventUiModels.cachedIn(viewModelScope)
                .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
                .collectLatest { initialUiModel.postValue(it) }
        }
    }

    private fun handleError(error: Throwable) {
        error.message?.let {
            _errorEvent.postValue(ErrorUiFavorite(it))
        }
    }
}