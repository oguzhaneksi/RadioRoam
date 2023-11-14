package com.radioroam.android.domain.util

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.radioroam.android.data.model.station.RadioStationDtoItem
import com.radioroam.android.data.network.NetworkResult
import com.radioroam.android.domain.state.RadioStationsUiState

fun NetworkResult<List<RadioStationDtoItem>>.map(): RadioStationsUiState {
    return when (this) {
        is NetworkResult.Success -> {
            if (data?.isNotEmpty() == true)
                RadioStationsUiState.Success(data.map { it.map() })
            else
                RadioStationsUiState.Error("No radio stations found!")
        }
        else -> {
            RadioStationsUiState.Error(errorMessage ?: "")
        }
    }
}

fun RadioStationDtoItem.map(): MediaItem {
    val metadata = MediaMetadata.Builder()
        .setDisplayTitle(name)
        .setArtworkUri(favicon?.toUri())
        .build()
    val uri = urlResolved?.toUri()
        ?.buildUpon()
        ?.scheme("https")
        ?.build()
    return MediaItem.Builder()
        .setUri(uri)
        .setMediaId(stationuuid.toString())
        .setMediaMetadata(metadata)
        .build()
}