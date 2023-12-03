package com.radioroam.android.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.radioroam.android.ui.components.HomeTopAppBar
import com.radioroam.android.ui.components.RadioStationPaginatedList
import com.radioroam.android.ui.navigation.Screen
import com.radioroam.android.ui.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    navController: NavController = rememberNavController(),
    onRadioStationClick: (Int) -> Unit = {},
    onNextPage: (List<MediaItem>) -> Unit = {},
    isPlayerSetUp: Boolean = false
) {
    val radioStations = viewModel.state.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            HomeTopAppBar {
                navController.navigate(Screen.Favorites.title)
            }
        }
    ) { paddingValues ->

        LaunchedEffect(radioStations.loadState.refresh, radioStations.loadState.append) {
            if (radioStations.loadState.append is LoadState.NotLoading) {
                // Update MediaBrowser with the current list of items
                val currentItems = radioStations.itemSnapshotList.items
                onNextPage(currentItems)
            }
        }

        RadioStationPaginatedList(
            modifier = Modifier
                .padding(paddingValues)
                .padding(bottom = if (isPlayerSetUp) 60.dp else 0.dp),
            items = radioStations,
            onItemClick = { index ->
                onRadioStationClick(index)
            },
            onFavClick = {
                viewModel.addOrRemoteFavorites(it)
            }
        )
    }


}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}