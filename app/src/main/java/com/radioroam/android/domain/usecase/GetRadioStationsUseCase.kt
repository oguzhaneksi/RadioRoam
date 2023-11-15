package com.radioroam.android.domain.usecase

import com.radioroam.android.data.repository.RadioStationRepository
import com.radioroam.android.domain.state.RadioStationsUiState
import com.radioroam.android.domain.util.map
import java.util.Locale

/**
 * Use case for retrieving radio stations.
 *
 * This use case encapsulates the business logic for fetching a list of radio stations
 * based on the device's default country setting. It leverages the RadioStationRepository
 * to perform the actual data fetching.
 *
 * @property repository RadioStationRepository instance used for data operations.
 */
class GetRadioStationsUseCase(
    private val repository: RadioStationRepository
) {

    /**
     * Executes the use case to fetch radio stations.
     *
     * This method triggers the process of fetching radio stations and transforms the
     * data into a UI-friendly format. It is designed to be called asynchronously from
     * the ViewModel layer.
     *
     * @param countryCode The ISO country code to filter the radio stations.
     * @return RadioStationsUiState representing the current state of UI related to radio stations.
     */
    suspend fun execute(
        countryCode: String = Locale.getDefault().country
    ): RadioStationsUiState {
        return repository.getRadioStationsByCountry(countryCode).map()
    }

}