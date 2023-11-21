package com.radioroam.android.domain.util

import androidx.compose.ui.text.capitalize
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.radioroam.android.data.model.station.RadioStationDtoItem
import com.radioroam.android.data.network.NetworkResult
import com.radioroam.android.data.util.ApiConstants
import com.radioroam.android.domain.state.RadioStationsUiState
import com.radioroam.android.ui.utility.removeNonAlphanumericFirstChar
import java.util.Locale

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
    val genres = (tags?.split(",") ?: emptyList())
        .filter { genre ->
            genre.length <= ApiConstants.MAX_GENRE_CHAR_LENGTH
        }
        .take(ApiConstants.MAX_GENRE_COUNT)
        .joinToString("|") { genre ->
            genre.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
    val metadata = MediaMetadata.Builder()
        .setDisplayTitle(name?.removeNonAlphanumericFirstChar())
        .setArtworkUri(favicon?.toUri())
        .setGenre(genres)
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