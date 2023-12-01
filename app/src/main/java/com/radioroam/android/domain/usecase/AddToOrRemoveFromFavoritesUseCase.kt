package com.radioroam.android.domain.usecase

import androidx.media3.common.MediaItem
import com.radioroam.android.data.repository.RadioStationRepository
import com.radioroam.android.domain.util.mapToDto
import kotlinx.coroutines.flow.firstOrNull

class AddToOrRemoveFromFavoritesUseCase(
    private val repository: RadioStationRepository
) {

    suspend fun execute(item: MediaItem) {
        val isAdded = repository.getFavoriteRadioStationByStationUUID(item.mediaId) != null
        val radioStationDtoItem = item.mapToDto()
        if (isAdded)
            repository.removeFromFavorites(radioStationDtoItem)
        else
            repository.addToFavorites(radioStationDtoItem)
    }

}