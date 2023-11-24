package com.radioroam.android.ui.utility

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.radioroam.android.ui.state.PlayerState

internal val Player.currentMediaItems: List<MediaItem> get() {
    return List(mediaItemCount, ::getMediaItemAt)
}

fun Player.updatePlaylist(incoming: List<MediaItem>) {
    val oldMediaIds = currentMediaItems.map { it.mediaId }.toSet()
    val itemsToAdd = incoming.filterNot { item -> item.mediaId in oldMediaIds }
    addMediaItems(itemsToAdd)
}

val PlayerState.isBuffering get() = playbackState == Player.STATE_BUFFERING