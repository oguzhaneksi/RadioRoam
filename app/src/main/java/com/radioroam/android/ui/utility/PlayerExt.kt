package com.radioroam.android.ui.utility

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.radioroam.android.ui.state.PlayerState

// Extension property for Player class to get the current media items.
// It returns a list of MediaItem objects.
internal val Player.currentMediaItems: List<MediaItem> get() {
    return List(mediaItemCount, ::getMediaItemAt)
}

// Extension function for Player class to update the playlist.
// It takes a list of incoming MediaItem objects, filters out the ones that are already in the playlist,
// logs the items to be added, and then adds them to the player.
fun Player.updatePlaylist(incoming: List<MediaItem>) {
    val oldMediaIds = currentMediaItems.map { it.mediaId }.toSet()
    val itemsToAdd = incoming.filterNot { item -> item.mediaId in oldMediaIds }
    Log.d("PlayerExt", "updatePlaylist: itemsToAdd: $itemsToAdd")
    addMediaItems(itemsToAdd)
}

// Extension function for Player class to play a media item at a specific index.
// If the current media item index is the same as the provided index, it does nothing.
// Otherwise, it seeks to the default position of the media item at the provided index,
// sets the player to play when ready, and prepares the player to recover from any errors
// that may have happened at previous media positions.
fun Player.playMediaAt(index: Int) {
    if (currentMediaItemIndex == index)
        return
    seekToDefaultPosition(index)
    playWhenReady = true
    // Recover from any errors that may have happened at previous media positions
    prepare()
}

// Extension property for PlayerState class to check if the player is buffering.
// It returns true if the playback state of the player is STATE_BUFFERING, false otherwise.
val PlayerState.isBuffering get() = playbackState == Player.STATE_BUFFERING