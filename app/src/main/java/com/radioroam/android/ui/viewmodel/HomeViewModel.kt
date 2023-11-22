package com.radioroam.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.radioroam.android.domain.usecase.GetRadioStationsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getRadioStationsUseCase: GetRadioStationsUseCase
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

    fun enablePlaying() {
        _isPlayerSetUp.update {
            true
        }
    }

}