package com.radioroam.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.radioroam.android.domain.state.RadioStationsUiState
import com.radioroam.android.ui.components.HomeTopAppBar
import com.radioroam.android.ui.components.RadioStationList
import com.radioroam.android.ui.components.player.CompactPlayerView
import com.radioroam.android.ui.components.player.ExpandedPlayerView
import com.radioroam.android.ui.components.player.rememberManagedExoPlayer
import com.radioroam.android.ui.state.PlayerState
import com.radioroam.android.ui.state.state
import com.radioroam.android.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            HomeTopAppBar()
        }
    ) { paddingValues ->
        when (state) {
            is RadioStationsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
            is RadioStationsUiState.Success -> {
                val items = (state as RadioStationsUiState.Success).data

                val player by rememberManagedExoPlayer()

                var playerState: PlayerState? by remember {
                    mutableStateOf(player?.state())
                }

                val currentPlayingStream by viewModel.currentPlayingMedia.collectAsStateWithLifecycle()

                DisposableEffect(currentPlayingStream, player) {
                    if (player != null && currentPlayingStream != null) {
                        player?.run {
                            setMediaItem(currentPlayingStream!!)
                            prepare()
                            playerState = player?.state()
                        }

                    }
                    onDispose {
                        playerState?.dispose()
                    }
                }

                val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

                val coroutineScope = rememberCoroutineScope()

                var openBottomSheet by remember { mutableStateOf(false) }

                if (openBottomSheet) {
                    ModalBottomSheet(
                        modifier = Modifier
                            .padding(paddingValues),
                        onDismissRequest = {
                            openBottomSheet = false
                        },
                        shape = RectangleShape,
                        sheetState = sheetState,
                        dragHandle = null,
                    ) {
                        ExpandedPlayerView(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface),
                            playerState = playerState!!,
                            onCollapseTap = {
                                coroutineScope.launch {
                                    sheetState.hide()
//                                    if (sheetState.isVisible.not())
                                        openBottomSheet = false
                                }/*.invokeOnCompletion {
                                    if (sheetState.isVisible.not())
                                        openBottomSheet = false
                                }*/
                            },
                            onMenuTap = {}
                        )
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    RadioStationList(
                        modifier = Modifier.padding(paddingValues),
                        items = items,
                        onItemClick = { item ->
                            viewModel.onMediaChanged(item)
                        }
                    )

                    if (playerState != null) {
                        CompactPlayerView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .align(Alignment.BottomCenter)
                                .clickable {
                                    coroutineScope.launch {
                                        sheetState.expand()
                                        openBottomSheet = true
                                    }/*.invokeOnCompletion {
                                        if (sheetState.isVisible)
                                            openBottomSheet = true
                                    }*/
                                },
                            playerState = playerState!!
                        )
                    }
                }

            }
            else -> {

            }
        }
    }


}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}