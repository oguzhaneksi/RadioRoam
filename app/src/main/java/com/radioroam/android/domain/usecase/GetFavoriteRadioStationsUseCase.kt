package com.radioroam.android.domain.usecase

import androidx.media3.common.MediaItem
import com.radioroam.android.data.repository.RadioStationRepository
import com.radioroam.android.domain.model.RadioStation
import com.radioroam.android.domain.util.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFavoriteRadioStationsUseCase(
    private val repository: RadioStationRepository
) {

    fun execute(): Flow<List<RadioStation>> = repository.getFavoriteRadioStations().map { list ->
        list.map {
            it.map(true)
        }
    }

}