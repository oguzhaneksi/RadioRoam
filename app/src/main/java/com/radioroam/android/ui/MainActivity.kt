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
import androidx.navigation.compose.rememberNavController
import com.radioroam.android.domain.util.toMediaItem
import com.radioroam.android.ui.components.mediacontroller.rememberManagedMediaController
import com.radioroam.android.ui.components.player.CompactPlayerView
import com.radioroam.android.ui.components.player.ExpandedPlayerView
import com.radioroam.android.ui.navigation.AppNavHost
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
                        // Create a SnackbarHostState for showing snackbars
                        val snackbarHostState = remember { SnackbarHostState() }

                        // Create a Scaffold with a SnackbarHost
                        Scaffold(
                            snackbarHost = {
                                SnackbarHost(snackbarHostState)
                            }
                        ) { paddingValues ->

                            // Observe the player setup state
                            val isPlayerSetUp by mainViewModel.isPlayerSetUp.collectAsStateWithLifecycle()

                            // Get the managed MediaController
                            val mediaController by rememberManagedMediaController()

                            // Prepare and play the media when the player is set up
                            LaunchedEffect(key1 = isPlayerSetUp) {
                                if (isPlayerSetUp) {
                                    mediaController?.run {
                                        if (mediaItemCount > 0) {
                                            prepare()
                                            play()
                                        }
                                    }
                                }
                            }

                            // Remember the player state
                            var playerState: PlayerState? by remember {
                                mutableStateOf(mediaController?.state())
                            }

                            // Update the player state when the MediaController changes
                            DisposableEffect(key1 = mediaController) {
                                mediaController?.run {
                                    playerState = state()
                                }
                                onDispose {
                                    playerState?.dispose()
                                }
                            }

                            // Show a snackbar when a player error occurs
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

                            // Remember the state of the bottom sheet
                            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

                            // Remember a CoroutineScope for launching coroutines
                            val coroutineScope = rememberCoroutineScope()

                            // Remember whether the bottom sheet is open
                            var openBottomSheet by remember { mutableStateOf(false) }

                            // Show the expanded player view in a bottom sheet when it's open
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

                            // Create a Box that fills the maximum size and has padding
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                            ) {
                                // Create the navigation host
                                AppNavHost(
                                    navController = navController,
                                    onNextPage = {
                                        // Update MediaController with the current list of items
                                        val currentItems = it
                                        mediaController?.updatePlaylist(currentItems.map { item -> item.toMediaItem() })
                                    },
                                    onRadioStationClick = { index ->
                                        mainViewModel.setupPlayer()
                                        mediaController?.playMediaAt(index)
                                    },
                                    isPlayerSetUp = isPlayerSetUp
                                )

                                // Show the compact player view when the player is set up
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