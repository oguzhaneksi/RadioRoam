package com.radioroam.android.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    onFavClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Radio Stations")
        },
        actions = {
            IconButton(onClick = onFavClick) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = "Favorites"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteTopAppBar(
    navigateUp: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Favorite Stations")
        },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}

@Preview
@Composable
fun HomeTopAppBarPreview() {
    HomeTopAppBar {

    }
}

@Preview
@Composable
fun FavoriteTopAppBarPreview() {
    FavoriteTopAppBar {

    }
}