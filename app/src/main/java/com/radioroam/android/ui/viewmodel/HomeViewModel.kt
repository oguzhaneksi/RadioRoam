package com.radioroam.android.ui.viewmodel

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.radioroam.android.domain.model.RadioStation
import com.radioroam.android.domain.usecase.AddToOrRemoveFromFavoritesUseCase
import com.radioroam.android.domain.usecase.GetRadioStationsUseCase
import com.radioroam.android.domain.util.FAVORITE_ARG
import com.radioroam.android.paginator.DefaultPaginator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getRadioStationsUseCase: GetRadioStationsUseCase,
    private val addToOrRemoveFromFavoritesUseCase: AddToOrRemoveFromFavoritesUseCase
): ViewModel() {

    private val _state = MutableStateFlow(ScreenState(isLoading = true))
    val state = _state.asStateFlow()

    private val paginator = DefaultPaginator(
        initialKey = state.value.page,
        onLoadUpdated = {
            _state.update { state ->
                state.copy(isLoading = it)
            }
        },
        onRequest = { nextPage ->
            getRadioStationsUseCase.execute(page = nextPage)
        },
        getNextKey = {
            state.value.page + 1
        },
        onError = {
            _state.update { state ->
                state.copy(error = it?.message)
            }
        },
        onSuccess = { items, newKey ->
            _state.update { state ->
                state.copy(
                    items = state.items + items,
                    page = newKey,
                    endReached = items.isEmpty()
                )
            }
        }
    )

    init {
        loadNextItems()
    }

    fun loadNextItems() {
        viewModelScope.launch(Dispatchers.IO) {
            paginator.loadNextItems()
        }
    }

    fun addOrRemoteFavorites(item: RadioStation) {
        viewModelScope.launch(Dispatchers.IO) {
            addToOrRemoveFromFavoritesUseCase.execute(item)
            // find the radio station by item id and update the favorite status
            _state.update { state ->
                state.copy(
                    items = state.items.map { radioStation ->
                        if (radioStation.id == item.id) {
                            radioStation.copy(isFavorite = !radioStation.isFavorite)
                        } else {
                            radioStation
                        }
                    }
                )
            }
        }
    }

}

data class ScreenState(
    val isLoading: Boolean = false,
    val items: List<RadioStation> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Int = 0
)