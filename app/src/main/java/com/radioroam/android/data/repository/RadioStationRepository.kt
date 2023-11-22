package com.radioroam.android.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.radioroam.android.data.datasource.RadioStationsRemoteDataSource
import com.radioroam.android.data.model.station.RadioStationDtoItem
import com.radioroam.android.data.network.ApiService
import com.radioroam.android.data.network.NetworkResult
import com.radioroam.android.data.network.safeApiCall
import com.radioroam.android.data.repository.paging.RadioStationsPagingSource
import com.radioroam.android.data.util.ApiConstants
import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow


class RadioStationRepository(
    private val dataSource: RadioStationsRemoteDataSource
) {

    fun getRadioStationsByCountry(
        isoCountryCode: String,
        pageSize: Int = ApiConstants.PAGE_SIZE
    ): Flow<PagingData<RadioStationDtoItem>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize, prefetchDistance = 2),
            pagingSourceFactory = {
                RadioStationsPagingSource(dataSource, isoCountryCode, pageSize)
            }
        ).flow
    }

}