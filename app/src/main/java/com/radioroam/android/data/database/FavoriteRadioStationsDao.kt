package com.radioroam.android.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.radioroam.android.data.model.station.RadioStationDtoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRadioStationsDao {
    @Query("SELECT * FROM RadioStationDtoItem ORDER BY name LIMIT :limit OFFSET :offset")
    fun getAllFavoriteRadioStations(limit: Int, offset: Int): Flow<List<RadioStationDtoItem>>

    @Query("SELECT stationuuid FROM RadioStationDtoItem WHERE stationuuid = :stationuuid")
    suspend fun getFavoriteRadioStationByStationUUID(stationuuid: String): RadioStationDtoItem?

    @Insert
    suspend fun insertRadioStations(vararg radioStations: RadioStationDtoItem)

    @Delete
    suspend fun deleteRadioStations(vararg radioStations: RadioStationDtoItem)
}