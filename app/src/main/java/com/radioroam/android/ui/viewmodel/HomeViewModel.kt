package com.radioroam.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.radioroam.android.domain.state.RadioStationsUiState
import com.radioroam.android.domain.usecase.GetRadioStationsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getRadioStationsUseCase: GetRadioStationsUseCase
): ViewModel() {

    private val _state = MutableStateFlow<RadioStationsUiState>(RadioStationsUiState.Loading)
    val state = _state.asStateFlow()

    private val _currentPlayingMedia = MutableStateFlow<MediaItem?>(null)
    val currentPlayingMedia = _currentPlayingMedia.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.emit(getRadioStationsUseCase.execute())
        }
    }

    fun onMediaChanged(newMedia: MediaItem) {
        _currentPlayingMedia.update {
            newMedia
        }
    }

    fun playPreviousStation() {
        _currentPlayingMedia.update { currentPlayingMedia ->
            if (state.value !is RadioStationsUiState.Success)
                currentPlayingMedia
            else {
                val mediaList = (state.value as RadioStationsUiState.Success).data
                val currentPlayingIndex = mediaList.indexOfFirst {
                    currentPlayingMedia?.mediaId == it.mediaId
                }
                if (currentPlayingIndex > 0)
                    mediaList.getOrNull(currentPlayingIndex - 1)
                else
                    currentPlayingMedia
            }
        }
    }

    fun playNextStation() {
        _currentPlayingMedia.update { currentPlayingMedia ->
            if (state.value !is RadioStationsUiState.Success)
                currentPlayingMedia
            else {
                val mediaList = (state.value as RadioStationsUiState.Success).data
                val currentPlayingIndex = mediaList.indexOfFirst {
                    currentPlayingMedia?.mediaId == it.mediaId
                }
                if (currentPlayingIndex >= 0 && currentPlayingIndex < mediaList.size)
                    mediaList.getOrNull(currentPlayingIndex + 1)
                else
                    currentPlayingMedia
            }
        }
    }

}