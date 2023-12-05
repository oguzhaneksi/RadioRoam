package com.radioroam.android.domain.usecase

import android.telephony.TelephonyManager
import androidx.media3.common.MediaItem
import androidx.paging.PagingData
import androidx.paging.map
import com.radioroam.android.data.repository.RadioStationRepository
import com.radioroam.android.domain.util.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class GetRadioStationsUseCase(
    private val repository: RadioStationRepository,
    private val telephonyManager: TelephonyManager
) {

    fun execute(
        countryCode: String = telephonyManager.networkCountryIso
    ): Flow<PagingData<MediaItem>> {
        val pagingDataFlow = repository.getRadioStationsByCountry(countryCode)
        val favoriteStations = repository.getFavoriteRadioStations()
        return pagingDataFlow.map { pagingData ->
            pagingData.map { radioStation ->
                radioStation.map(favoriteStations.first().any { it.stationuuid == radioStation.stationuuid })
            }
        }
    }

}