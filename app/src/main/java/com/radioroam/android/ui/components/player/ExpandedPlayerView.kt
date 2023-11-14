package com.radioroam.android.ui.components.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.radioroam.android.R
import com.radioroam.android.ui.state.PlayerState

@Composable
fun ExpandedPlayerView(
    modifier: Modifier = Modifier,
    playerState: PlayerState,
    onCollapseTap: () -> Unit,
    onMenuTap: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onCollapseTap) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(id = R.drawable.keyboard_arrow_down),
                    contentDescription = "Collapse",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = onMenuTap) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = "More",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(8.dp),
            model = playerState.currentMediaItem?.mediaMetadata?.artworkUri,
            contentDescription = null,
        )
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = playerState.currentMediaItem?.mediaMetadata?.displayTitle.toString(),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1, // prevent text from wrapping
            overflow = TextOverflow.Ellipsis // add ellipsis when text is too long
        )
//        TimeBar(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp),
//            player = playerState.player
//        )
        PlayPauseButton(
            modifier = Modifier
                .size(48.dp),
            isPlaying = playerState.isPlaying
        ) {
            with(playerState.player) {
                playWhenReady = !playWhenReady
            }
        }
    }
}