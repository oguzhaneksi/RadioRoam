package com.radioroam.android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.radioroam.android.data.model.station.RadioStationDtoItem

@Database(entities = [RadioStationDtoItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val radioStationsDao: FavoriteRadioStationsDao
}