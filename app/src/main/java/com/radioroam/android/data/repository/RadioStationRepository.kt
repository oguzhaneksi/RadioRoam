package com.radioroam.android.data.repository

import com.radioroam.android.data.model.station.RadioStationDtoItem
import com.radioroam.android.data.network.ApiService
import com.radioroam.android.data.network.NetworkResult
import com.radioroam.android.data.network.safeApiCall
import com.radioroam.android.data.util.ApiConstants
import io.ktor.client.call.body

/**
 * Repository for fetching radio station data.
 *
 * This class provides an abstraction layer between the network data sources and
 * the rest of the application. It handles operations related to fetching radio
 * station information based on various criteria.
 *
 * @property api ApiService instance used for network operations.
 */
class RadioStationRepository(
    private val api: ApiService
) {

    /**
     * Fetches a list of radio stations based on the provided country code.
     *
     * This method makes a network request to retrieve radio stations and handles
     * the response, encapsulating it in a NetworkResult. It abstracts the underlying
     * network logic and provides a clean interface to the UI layer.
     *
     * @param isoCountryCode The ISO country code to filter the radio stations.
     * @param pageIndex The number of the page to filter the radio stations.
     * @param pageSize The size of the page to limit the response of the radio stations.
     * @return NetworkResult containing a list of RadioStationDtoItem or an error.
     */
    suspend fun getRadioStationsByCountry(
        isoCountryCode: String,
        pageIndex: Int = 0,
        pageSize: Int = ApiConstants.PAGE_SIZE
    ): NetworkResult<List<RadioStationDtoItem>> {
        return safeApiCall {
            api.getRadioStationsByCountry(isoCountryCode, pageSize, pageIndex * pageSize, true).body() as List<RadioStationDtoItem>
        }
    }

}