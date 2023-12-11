package com.radioroam.android.ui.components.player

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.ui.R
import coil.compose.AsyncImage

@Composable
fun PlayPauseButton(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    isBuffering: Boolean,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    onTogglePlayPause: () -> Unit
) {
    if (isBuffering) {
        PlayerProgressIndicator(
            modifier = modifier,
            progressTint = iconTint
        )
    }
    else {
        IconButton(
            modifier = modifier,
            onClick = {
                onTogglePlayPause()
            }
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(fraction = .8f),
                painter = painterResource(
                    id = if (isPlaying)
                        R.drawable.exo_icon_pause
                    else
                        R.drawable.exo_icon_play
                ),
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = iconTint
            )
        }
    }
}

@Composable
fun PlayerProgressIndicator(
    modifier: Modifier = Modifier,
    progressTint: Color = MaterialTheme.colorScheme.onSurface
) {
    Box(
        modifier = modifier
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize(.8f)
                .align(Center),
            color = progressTint,
            trackColor = Color.Transparent
        )
    }
}

@Composable
fun MiniPlayerArtworkView(
    modifier: Modifier = Modifier,
    artworkUri: Uri?
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp)
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = artworkUri,
            contentDescription = null,
        )
    }
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

@Composable
fun PreviousButton(
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(fraction = .8f),
            painter = painterResource(R.drawable.exo_icon_previous),
            contentDescription = "Previous",
            tint = iconTint
        )
    }
}

@Composable
fun NextButton(
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(fraction = .8f),
            painter = painterResource(R.drawable.exo_icon_next),
            contentDescription = "Previous",
            tint = iconTint
        )
    }
}