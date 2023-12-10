package com.radioroam.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.media3.common.MediaItem
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.radioroam.android.domain.model.RadioStation
import com.radioroam.android.ui.screens.FavoritesScreen
import com.radioroam.android.ui.screens.HomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    onNextPage: (List<RadioStation>) -> Unit = {},
    onRadioStationClick: (Int) -> Unit = {},
    isPlayerSetUp: Boolean = false
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.title
    ) {
        composable(route = Screen.Home.title) {
            HomeScreen(
                navController = navController,
                onNextPage = onNextPage,
                onRadioStationClick = onRadioStationClick,
                isPlayerSetUp = isPlayerSetUp
            )
        }
        composable(route = Screen.Favorites.title) {
            FavoritesScreen(
                navController = navController
            )
        }
    }
}