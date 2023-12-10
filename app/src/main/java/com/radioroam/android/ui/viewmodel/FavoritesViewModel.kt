package com.radioroam.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radioroam.android.domain.model.RadioStation
import com.radioroam.android.domain.usecase.AddToOrRemoveFromFavoritesUseCase
import com.radioroam.android.domain.usecase.GetFavoriteRadioStationsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavoriteRadioStationsUseCase: GetFavoriteRadioStationsUseCase,
    private val addToOrRemoveFromFavoritesUseCase: AddToOrRemoveFromFavoritesUseCase
): ViewModel() {
    private val _favorites = MutableStateFlow<List<RadioStation>>(emptyList())
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

    fun removeItem(item: RadioStation) {
        viewModelScope.launch(Dispatchers.IO) {
            addToOrRemoveFromFavoritesUseCase.execute(item)
        }
    }


}