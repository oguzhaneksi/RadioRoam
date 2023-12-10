package com.radioroam.android.ui.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import coil.compose.AsyncImage
import com.radioroam.android.R
import com.radioroam.android.domain.model.RadioStation

@Composable
fun RadioStationRow(
    modifier: Modifier = Modifier,
    item: RadioStation,
    isFavorite: Boolean,
    onFavClick: (RadioStation) -> Unit
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
                model = item.favicon,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.radio),
                error = painterResource(id = R.drawable.error)
            )
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f), // give weight to the text
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.name,
                    color = Color.Black,
                    maxLines = 1, // prevent text from wrapping
                    overflow = TextOverflow.Ellipsis // add ellipsis when text is too long
                )
                val genres = item.genres
                if (genres.isNotEmpty()) {
                    Text(
                        text = genres.joinToString("|"),
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        IconButton(onClick = { onFavClick(item)}) {
            Icon(
                imageVector = if (isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites"
            )
        }
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
        item = RadioStation(
            id = "1",
            name = "Radio Station",
            url = Uri.EMPTY,
            favicon = Uri.EMPTY,
            genres = listOf("Pop", "Rock"),
            isFavorite = false
        ),
        isFavorite = false,
        onFavClick = {}
    )
}