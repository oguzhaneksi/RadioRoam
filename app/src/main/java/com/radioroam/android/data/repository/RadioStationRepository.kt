package com.radioroam.android.data.repository

import com.radioroam.android.data.model.station.RadioStationDtoItem
import com.radioroam.android.data.network.ApiService
import com.radioroam.android.data.network.NetworkResult
import com.radioroam.android.data.network.safeApiCall
import io.ktor.client.call.body

class RadioStationRepository(
    private val api: ApiService
) {

    suspend fun getRadioStationsByCountry(
        isoCountryCode: String
    ): NetworkResult<List<RadioStationDtoItem>> {
        return safeApiCall {
            api.getRadioStationsByCountry(isoCountryCode, 10, 0, true).body() as List<RadioStationDtoItem>
        }
    }

}