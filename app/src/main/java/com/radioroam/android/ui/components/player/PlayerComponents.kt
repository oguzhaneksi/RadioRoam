package com.radioroam.android.ui.components.player

import android.net.Uri
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.media3.common.Player
import androidx.media3.ui.R
import coil.compose.AsyncImage

@Composable
fun PlayPauseButton(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onTogglePlayPause: () -> Unit
) {
    IconButton(
        onClick = {
            onTogglePlayPause()
        }
    ) {
        Icon(
            modifier = modifier,
            painter = painterResource(
                id = if (isPlaying)
                    R.drawable.exo_icon_pause
                else
                    R.drawable.exo_icon_play
            ),
            contentDescription = if (isPlaying) "Pause" else "Play",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun MiniPlayerArtworkView(
    modifier: Modifier = Modifier,
    artworkUri: Uri?
) {
    AsyncImage(
        modifier = modifier,
        model = artworkUri,
        contentDescription = null,
    )
}

@Composable
fun TimeBar(
    modifier: Modifier = Modifier,
    player: Player
) {
    val currentPosition = remember { mutableLongStateOf(0L) }
    val duration = remember { mutableLongStateOf(0L) }
    LaunchedEffect(player) {
        snapshotFlow { player.currentPosition }
            .collect { currentPosition.longValue = it }

        snapshotFlow { player.duration }
            .collect { duration.longValue = it }
    }

    Slider(
        modifier = modifier,
        value = currentPosition.longValue.toFloat(),
        onValueChange = {

        },
        valueRange = 0f..duration.longValue.toFloat()
    )
}