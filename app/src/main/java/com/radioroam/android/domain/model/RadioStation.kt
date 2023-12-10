package com.radioroam.android.domain.model

import android.net.Uri

data class RadioStation(
    val id: String,
    val name: String,
    val url: Uri,
    val favicon: Uri,
    val isFavorite: Boolean,
    val genres: List<String>
)
