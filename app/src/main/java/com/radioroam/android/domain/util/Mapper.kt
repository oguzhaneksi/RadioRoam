package com.radioroam.android.domain.util

import android.net.Uri
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.radioroam.android.data.model.station.RadioStationDtoItem
import com.radioroam.android.data.util.ApiConstants
import com.radioroam.android.domain.model.RadioStation
import com.radioroam.android.ui.utility.removeNonAlphanumericFirstChar
import java.util.Locale

fun RadioStationDtoItem.map(isFavorite: Boolean): RadioStation {
    val genres = (tags?.split(",") ?: emptyList())
        .filter { genre ->
            genre.length <= ApiConstants.MAX_GENRE_CHAR_LENGTH
        }
        .take(ApiConstants.MAX_GENRE_COUNT)
        .map { genre ->
            genre.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
    return RadioStation(
        id = stationuuid,
        name = name?.removeNonAlphanumericFirstChar() ?: "",
        url = urlResolved?.toUri()?.buildUpon()?.scheme("https")?.build() ?: Uri.EMPTY,
        favicon = favicon?.toUri()?.buildUpon()?.scheme("https")?.build() ?: Uri.EMPTY,
        isFavorite = isFavorite,
        genres = genres
    )
}

fun RadioStation.mapToDto(): RadioStationDtoItem {
    return RadioStationDtoItem(
        urlResolved = url.toString(),
        stationuuid = id,
        favicon = favicon.toString(),
        name = name,
        tags = genres.joinToString(",")
    )
}

fun RadioStation.toMediaItem(): MediaItem {
    val genres = genres.joinToString("|")
    val metadata = MediaMetadata.Builder()
        .setDisplayTitle(name.removeNonAlphanumericFirstChar())
        .setArtworkUri(favicon)
        .setGenre(genres)
        .build()
    return MediaItem.Builder()
        .setUri(url)
        .setMediaId(id)
        .setMediaMetadata(metadata)
        .build()
}