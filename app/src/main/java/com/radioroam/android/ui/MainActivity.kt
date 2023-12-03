package com.radioroam.android.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import com.radioroam.android.ui.components.mediabrowser.rememberManagedMediaBrowser
import com.radioroam.android.ui.components.mediacontroller.rememberManagedMediaController
import com.radioroam.android.ui.components.player.CompactPlayerView
import com.radioroam.android.ui.components.player.ExpandedPlayerView
import com.radioroam.android.ui.navigation.Screen
import com.radioroam.android.ui.screens.FavoritesScreen
import com.radioroam.android.ui.screens.HomeScreen
import com.radioroam.android.ui.state.PlayerState
import com.radioroam.android.ui.state.state
import com.radioroam.android.ui.theme.AppTheme
import com.radioroam.android.ui.utility.playMediaAt
import com.radioroam.android.ui.utility.updatePlaylist
import com.radioroam.android.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoinContext {
                AppTheme {
                    // A surface container using the 'background' color from the theme
                    val navController = rememberNavController()
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val snackbarHostState = remember { SnackbarHostState() }
                        Scaffold(
                            snackbarHost = {
                                SnackbarHost(snackbarHostState)
                            }
                        ) {
                            val isPlayerSetUp by mainViewModel.isPlayerSetUp.collectAsStateWithLifecycle()

                            val browser by rememberManagedMediaBrowser()

                            LaunchedEffect(key1 = isPlayerSetUp) {
                                if (isPlayerSetUp) {
                                    browser?.run {
                                        if (mediaItemCount > 0) {
                                            prepare()
                                            play()
                                        }
                                    }
                                }
                            }

                            val mediaController by rememberManagedMediaController()

                            var playerState: PlayerState? by remember {
                                mutableStateOf(mediaController?.state())
                            }

                            DisposableEffect(key1 = mediaController) {
                                mediaController?.run {
                                    playerState = state()
                                }
                                onDispose {
                                    playerState?.dispose()
                                }
                            }

                            LaunchedEffect(key1 = playerState?.playerError) {
                                playerState?.playerError?.let { exception ->
                                    val result = snackbarHostState.showSnackbar(
                                        message = "${exception.message}, Code: ${exception.errorCode}",
                                        withDismissAction = true,
                                        actionLabel = "Retry"
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        mediaController?.prepare()
                                    }
                                }
                            }

                            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

                            val coroutineScope = rememberCoroutineScope()

                            var openBottomSheet by remember { mutableStateOf(false) }

                            if (openBottomSheet && playerState != null) {
                                ModalBottomSheet(
                                    onDismissRequest = {
                                        openBottomSheet = false
                                    },
                                    shape = RectangleShape,
                                    sheetState = sheetState,
                                ) {
                                    ExpandedPlayerView(
                                        modifier = Modifier,
                                        playerState = playerState!!,
                                        onCollapseTap = {
                                            coroutineScope.launch {
                                                sheetState.hide()
                                                openBottomSheet = false
                                            }
                                        },
                                        onMenuTap = {},
                                        onPrevClick = {
                                            mediaController?.seekToPreviousMediaItem()
                                        },
                                        onNextClick = {
                                            mediaController?.seekToNextMediaItem()
                                        }
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier.fillMaxSize()
                                    .padding(it)
                            ) {
                                NavHost(
                                    navController = navController,
                                    startDestination = Screen.Home.title
                                ) {
                                    composable(route = Screen.Home.title) {
                                        HomeScreen(
                                            navController = navController,
                                            onNextPage = {
                                                // Update MediaBrowser with the current list of items
                                                val currentItems = it
                                                browser?.updatePlaylist(currentItems)
                                            },
                                            onRadioStationClick = { index ->
                                                mainViewModel.setupPlayer()
                                                mediaController?.playMediaAt(index)
                                            },
                                            isPlayerSetUp = isPlayerSetUp
                                        )
                                    }
                                    composable(route = Screen.Favorites.title) {
                                        FavoritesScreen(
                                            navController = navController
                                        )
                                    }
                                }

                                if (isPlayerSetUp && playerState != null) {
                                    CompactPlayerView(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(60.dp)
                                            .align(Alignment.BottomCenter)
                                            .clickable {
                                                coroutineScope.launch {
                                                    sheetState.expand()
                                                    openBottomSheet = true
                                                }
                                            },
                                        playerState = playerState!!
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}