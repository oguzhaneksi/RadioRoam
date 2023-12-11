package com.radioroam.android.ui.components.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.radioroam.android.R
import com.radioroam.android.ui.state.PlayerState
import com.radioroam.android.ui.utility.isBuffering

@Composable
fun ExpandedPlayerView(
    modifier: Modifier = Modifier,
    playerState: PlayerState,
    onCollapseTap: () -> Unit,
    onMenuTap: () -> Unit,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(
            onCollapseTap = onCollapseTap,
            onMenuTap = onMenuTap
        )
        PlayerArtwork(playerState = playerState)
        PlayerTitle(playerState = playerState)
        PlayerControls(
            playerState = playerState,
            onPrevClick = onPrevClick,
            onNextClick = onNextClick
        )
    }
}

@Composable
private fun TopBar(onCollapseTap: () -> Unit, onMenuTap: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onCollapseTap) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = R.drawable.keyboard_arrow_down),
                contentDescription = "Collapse",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        IconButton(onClick = onMenuTap) {
            Icon(
                modifier = Modifier.size(40.dp),
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = "More options",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun PlayerArtwork(playerState: PlayerState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(32.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            model = playerState.currentMediaItem?.mediaMetadata?.artworkUri,
            contentDescription = null,
        )
    }
}

@Composable
private fun PlayerTitle(playerState: PlayerState) {
    Text(
        modifier = Modifier
            .padding(8.dp),
        text = playerState.currentMediaItem?.mediaMetadata?.displayTitle.toString(),
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1, // prevent text from wrapping
        overflow = TextOverflow.Ellipsis // add ellipsis when text is too long
    )
}

@Composable
private fun PlayerControls(
    playerState: PlayerState,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PreviousButton(
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp),
            iconTint = MaterialTheme.colorScheme.onSurface
        ) {
            onPrevClick()
        }
        PlayPauseButton(
            modifier = Modifier
                .size(64.dp)
                .background(color = MaterialTheme.colorScheme.onSurface, shape = CircleShape)
                .padding(8.dp),
            isPlaying = playerState.isPlaying,
            isBuffering = playerState.isBuffering,
            iconTint = MaterialTheme.colorScheme.surface
        ) {
            with(playerState.player) {
                playWhenReady = !playWhenReady
            }
        }
        NextButton(
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp),
            iconTint = MaterialTheme.colorScheme.onSurface
        ) {
            onNextClick()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTopBar() {
    TopBar(
        onCollapseTap = { /* Collapse action */ },
        onMenuTap = { /* Menu action */ }
    )
}