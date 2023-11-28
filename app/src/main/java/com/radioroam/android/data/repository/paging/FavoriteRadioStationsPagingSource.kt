package com.radioroam.android.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import coil.network.HttpException
import com.radioroam.android.data.database.AppDatabase
import com.radioroam.android.data.model.station.RadioStationDtoItem
import kotlinx.coroutines.flow.firstOrNull
import java.io.IOException

class FavoriteRadioStationsPagingSource(
    private val database: AppDatabase,
    private val pageSize: Int
) : PagingSource<Int, RadioStationDtoItem>() {

    companion object {
        private const val INITIAL_PAGE_INDEX = 0
    }
    override fun getRefreshKey(state: PagingState<Int, RadioStationDtoItem>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RadioStationDtoItem> {
        return try {
            val currentPage = params.key ?: INITIAL_PAGE_INDEX
            val result = database.radioStationsDao.getAllFavoriteRadioStations(
                limit = pageSize,
                offset = currentPage * pageSize
            )
            val list = result.firstOrNull() ?: emptyList()
            LoadResult.Page(
                data = list,
                prevKey = if (currentPage == INITIAL_PAGE_INDEX) null else currentPage - 1,
                nextKey = if (list.isEmpty()) null else currentPage + 1
            )

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }


}