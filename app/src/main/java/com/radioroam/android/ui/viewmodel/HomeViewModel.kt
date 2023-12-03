package com.radioroam.android.ui.viewmodel

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.radioroam.android.domain.usecase.AddToOrRemoveFromFavoritesUseCase
import com.radioroam.android.domain.usecase.GetRadioStationsUseCase
import com.radioroam.android.domain.util.FAVORITE_ARG
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

    private val _state = MutableStateFlow<PagingData<MediaItem>>(PagingData.empty())
    val state = _state.asStateFlow()

    private val _isPlayerSetUp = MutableStateFlow(false)
    val isPlayerSetUp = _isPlayerSetUp.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getRadioStationsUseCase.execute()
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    _state.emit(it)
                }
        }
    }

    fun setupPlayer() {
        _isPlayerSetUp.update {
            true
        }
    }

    fun addOrRemoteFavorites(item: MediaItem) {
        viewModelScope.launch(Dispatchers.IO) {
            addToOrRemoveFromFavoritesUseCase.execute(item)
            // update the favorite state of each item in the paging data and trigger a recomposition
            _state.update {
                it.map { mediaItem ->
                    if (mediaItem.mediaId == item.mediaId) {
                        mediaItem.toggleFavoriteState()
                    } else {
                        mediaItem
                    }
                }
            }
        }
    }

    private fun MediaItem.toggleFavoriteState() = buildUpon().setMediaMetadata(
        mediaMetadata.buildUpon().setExtras(
            bundleOf(FAVORITE_ARG to !mediaMetadata.extras?.getBoolean(FAVORITE_ARG)!!)
        ).build()
    ).build()

}