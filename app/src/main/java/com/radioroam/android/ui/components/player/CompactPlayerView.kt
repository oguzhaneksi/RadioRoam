package com.radioroam.android.ui.components.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.radioroam.android.ui.state.PlayerState
import com.radioroam.android.ui.utility.isBuffering

@Composable
fun CompactPlayerView(
    modifier: Modifier = Modifier,
    playerState: PlayerState
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f) // give weight to text and image row
                    .padding(end = 8.dp), // add padding to ensure space for the icon
                verticalAlignment = Alignment.CenterVertically
            ) {
                val currentMediaItem = playerState.currentMediaItem
                if (currentMediaItem != null) {
                    MiniPlayerArtworkView(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(4.dp),
                        artworkUri = currentMediaItem.mediaMetadata.artworkUri
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f), // give weight to the text
                        text = currentMediaItem.mediaMetadata.displayTitle.toString(),
                        color = Color.Black,
                        maxLines = 1, // prevent text from wrapping
                        overflow = TextOverflow.Ellipsis // add ellipsis when text is too long
                    )
                }
            }
            PlayPauseButton(
                modifier = Modifier
                    .size(40.dp),
                isPlaying = playerState.isPlaying,
                isBuffering = playerState.isBuffering
            ) {
                with(playerState.player) {
                    playWhenReady = !playWhenReady
                }
            }

        }
    }
}