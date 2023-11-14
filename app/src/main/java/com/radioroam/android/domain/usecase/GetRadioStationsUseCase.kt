package com.radioroam.android.domain.usecase

import com.radioroam.android.data.repository.RadioStationRepository
import com.radioroam.android.domain.state.RadioStationsUiState
import com.radioroam.android.domain.util.map
import java.util.Locale

class GetRadioStationsUseCase(
    private val repository: RadioStationRepository
) {

    suspend fun execute(): RadioStationsUiState {
        return repository.getRadioStationsByCountry(Locale.getDefault().country).map()
    }

}