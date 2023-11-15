package com.radioroam.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import coil.compose.AsyncImage

@Composable
fun RadioStationRow(
    modifier: Modifier = Modifier,
    item: MediaItem
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f) // give weight to text and image row
                .padding(end = 8.dp), // add padding to ensure space for the icon
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.2f)
                    .aspectRatio(1f)
                    .padding(8.dp),
                model = item.mediaMetadata.artworkUri?.toString(),
                contentDescription = null,
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f), // give weight to the text
                text = item.mediaMetadata.displayTitle.toString(),
                color = Color.Black,
                maxLines = 1, // prevent text from wrapping
                overflow = TextOverflow.Ellipsis // add ellipsis when text is too long
            )
        }
        Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "Add to Favorites")
    }
}

@Preview
@Composable
fun RadioStationRowPreview() {
    RadioStationRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.White)
            .padding(16.dp),
        item = MediaItem.Builder()
            .setMediaMetadata(MediaMetadata.Builder().setDisplayTitle("Stationasdsadsadsaadsadssaddasdasadssaddsaadsdas").build())
            .build()
    )
}