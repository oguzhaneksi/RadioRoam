package com.radioroam.android.data.repository

import com.radioroam.android.data.database.AppDatabase
import com.radioroam.android.data.datasource.RadioStationsRemoteDataSource
import com.radioroam.android.data.model.station.RadioStationDtoItem
import com.radioroam.android.data.network.NetworkResult
import com.radioroam.android.data.util.ApiConstants


class RadioStationRepository(
    private val dataSource: RadioStationsRemoteDataSource,
    private val localDatabase: AppDatabase
) {

    suspend fun getRadioStationsByCountry(
        isoCountryCode: String,
        page: Int = 0,
        pageSize: Int = ApiConstants.PAGE_SIZE
    ): NetworkResult<List<RadioStationDtoItem>> {
        return dataSource.getRadioStationsByCountry(
            isoCountryCode = isoCountryCode,
            pageIndex = page,
            pageSize = pageSize
        )
    }

    fun getFavoriteRadioStations(
        pageSize: Int = ApiConstants.PAGE_SIZE
    ) = localDatabase.radioStationsDao.getAllFavoriteRadioStations(
        limit = pageSize,
        offset = 0
    )

    suspend fun getFavoriteRadioStationByStationUUID(stationuuid: String) =
        localDatabase.radioStationsDao.getFavoriteRadioStationByStationUUID(stationuuid)

    suspend fun addToFavorites(station: RadioStationDtoItem) {
        localDatabase.radioStationsDao.insertRadioStations(station)
    }

    suspend fun removeFromFavorites(station: RadioStationDtoItem) {
        localDatabase.radioStationsDao.deleteRadioStations(station)
    }

}