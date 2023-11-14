package com.radioroam.android.domain.state

import androidx.media3.common.MediaItem

sealed interface RadioStationsUiState {
    data object Loading: RadioStationsUiState
    data class Success(val data: List<MediaItem>): RadioStationsUiState
    data class Error(val message: String): RadioStationsUiState
}