package com.radioroam.android.domain.usecase

import android.telephony.TelephonyManager
import androidx.media3.common.MediaItem
import com.radioroam.android.data.network.NetworkResult
import com.radioroam.android.data.repository.RadioStationRepository
import com.radioroam.android.domain.model.RadioStation
import com.radioroam.android.domain.util.map
import kotlinx.coroutines.flow.firstOrNull

class GetRadioStationsUseCase(
    private val repository: RadioStationRepository,
    private val telephonyManager: TelephonyManager
) {

    suspend fun execute(
        countryCode: String = telephonyManager.networkCountryIso,
        page: Int = 0
    ): Result<List<RadioStation>> {
        val radioStationsFromApi = repository.getRadioStationsByCountry(countryCode, page)
        val favoriteStations = repository.getFavoriteRadioStations().firstOrNull()
        return when (radioStationsFromApi) {
            is NetworkResult.Success -> {
                Result.success(
                    radioStationsFromApi.data?.map { radioStation ->
                        radioStation.map(favoriteStations?.any { it.stationuuid == radioStation.stationuuid } == true)
                    } ?: emptyList()
                )
            }
            is NetworkResult.Error -> {
                Result.failure(Exception(radioStationsFromApi.errorMessage))
            }
        }
    }

}