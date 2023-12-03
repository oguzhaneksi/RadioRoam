package com.radioroam.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.radioroam.android.domain.usecase.AddToOrRemoveFromFavoritesUseCase
import com.radioroam.android.domain.usecase.GetFavoriteRadioStationsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavoriteRadioStationsUseCase: GetFavoriteRadioStationsUseCase,
    private val addToOrRemoveFromFavoritesUseCase: AddToOrRemoveFromFavoritesUseCase
): ViewModel() {
    private val _favorites = MutableStateFlow<List<MediaItem>>(emptyList())
    val favorites = _favorites.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getFavoriteRadioStationsUseCase.execute()
                .distinctUntilChanged()
                .collect {
                    _favorites.emit(it)
                }
        }
    }

    fun removeItem(item: MediaItem) {
        viewModelScope.launch(Dispatchers.IO) {
            addToOrRemoveFromFavoritesUseCase.execute(item)
        }
    }


}