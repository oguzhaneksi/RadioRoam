package com.radioroam.android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.C
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.radioroam.android.ui.components.HomeTopAppBar
import com.radioroam.android.ui.components.RadioStationList
import com.radioroam.android.ui.components.mediabrowser.rememberManagedMediaBrowser
import com.radioroam.android.ui.components.mediacontroller.rememberManagedMediaController
import com.radioroam.android.ui.components.player.CompactPlayerView
import com.radioroam.android.ui.components.player.ExpandedPlayerView
import com.radioroam.android.ui.state.PlayerState
import com.radioroam.android.ui.state.state
import com.radioroam.android.ui.utility.updatePlaylist
import com.radioroam.android.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val radioStations = viewModel.state.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            HomeTopAppBar()
        }
    ) { paddingValues ->
        val isPlayerSetUp by viewModel.isPlayerSetUp.collectAsStateWithLifecycle()

        val browser by rememberManagedMediaBrowser()

        LaunchedEffect(radioStations.loadState.refresh, radioStations.loadState.append) {
            browser?.run {
                if (radioStations.loadState.append is LoadState.NotLoading) {
                    // Update MediaBrowser with the current list of items
                    val currentItems = radioStations.itemSnapshotList.items
                    updatePlaylist(currentItems)
                }
            }
        }

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

        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        val coroutineScope = rememberCoroutineScope()

        var openBottomSheet by remember { mutableStateOf(false) }

        if (openBottomSheet) {
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
        ) {
            RadioStationList(
                modifier = Modifier.padding(paddingValues),
                items = radioStations,
                onItemClick = { index ->
                    viewModel.enablePlaying()
                    mediaController?.seekToDefaultPosition(index)
                }
            )

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

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}