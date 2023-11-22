package com.radioroam.android.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import coil.network.HttpException
import com.radioroam.android.data.datasource.RadioStationsRemoteDataSource
import com.radioroam.android.data.model.station.RadioStationDtoItem
import com.radioroam.android.data.network.NetworkResult
import java.io.IOException

class RadioStationsPagingSource(
    private val dataSource: RadioStationsRemoteDataSource,
    private val isoCountryCode: String,
    private val pageSize: Int
): PagingSource<Int, RadioStationDtoItem>() {

    companion object {
        private const val INITIAL_PAGE_INDEX = 0
    }

    override fun getRefreshKey(state: PagingState<Int, RadioStationDtoItem>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RadioStationDtoItem> {
        return try {
            val currentPage = params.key ?: INITIAL_PAGE_INDEX
            val result = dataSource.getRadioStationsByCountry(
                isoCountryCode = isoCountryCode,
                pageIndex = currentPage,
                pageSize = pageSize
            )
            when (result) {
                is NetworkResult.Success -> {
                    val list = result.data ?: emptyList()
                    LoadResult.Page(
                        data = list,
                        prevKey = if (currentPage == INITIAL_PAGE_INDEX) null else currentPage - 1,
                        nextKey = if (list.isEmpty()) null else currentPage + 1
                    )
                }
                else -> {
                    LoadResult.Error(
                        throwable = Exception(result.errorMessage)
                    )
                }
            }

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}