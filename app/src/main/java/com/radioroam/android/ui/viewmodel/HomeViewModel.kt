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

}