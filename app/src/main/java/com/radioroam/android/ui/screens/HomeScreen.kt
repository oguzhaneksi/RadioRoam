package com.radioroam.android.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.radioroam.android.domain.model.RadioStation
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
    onNextPage: (List<RadioStation>) -> Unit = {},
    isPlayerSetUp: Boolean = false
) {

    Scaffold(
        topBar = {
            HomeTopAppBar {
                navController.navigate(Screen.Favorites.title)
            }
        }
    ) { paddingValues ->

        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(state.page) {
            onNextPage(state.items)
        }

        RadioStationPaginatedList(
            modifier = Modifier
                .padding(paddingValues)
                .padding(bottom = if (isPlayerSetUp) 60.dp else 0.dp),
            state = state,
            onItemClick = { index ->
                onRadioStationClick(index)
            },
            onFavClick = {
                viewModel.addOrRemoteFavorites(it)
            },
            onNextPage = {
                viewModel.loadNextItems()
            }
        )
    }


}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}