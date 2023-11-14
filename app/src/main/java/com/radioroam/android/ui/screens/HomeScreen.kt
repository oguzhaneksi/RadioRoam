package com.radioroam.android.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import com.radioroam.android.domain.state.RadioStationsUiState
import com.radioroam.android.ui.components.RadioStationList
import com.radioroam.android.ui.components.player.CompactPlayerView
import com.radioroam.android.ui.components.player.ExpandedPlayerView
import com.radioroam.android.ui.components.player.PlayerView
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

    when (state) {
        is RadioStationsUiState.Loading -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Radio Stations")
                        },
                    )
                }
            ) { paddingValues ->
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

            val sheetState = rememberStandardBottomSheetState(
                initialValue = SheetValue.Hidden,
                skipHiddenState = false
            )

            val sheetScaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = sheetState
            )

            val coroutineScope = rememberCoroutineScope()

            BackHandler(sheetState.currentValue == SheetValue.Expanded) {
                coroutineScope.launch {
                    sheetState.hide()
                }
            }

            val sheetShape = if (sheetState.currentValue == SheetValue.Expanded)
                RoundedCornerShape(0.dp)
            else
                RoundedCornerShape(8.dp)

            BottomSheetScaffold(
                sheetShape = sheetShape,
                sheetSwipeEnabled = false,
                sheetDragHandle = null,
                sheetContent = {
                    if (playerState != null) {
                        val configuration = LocalConfiguration.current

                        val screenHeight = configuration.screenHeightDp.dp
//                        val heightInDp by animateDpAsState(
//                            targetValue = when (sheetState.currentValue) {
//                                SheetValue.Expanded -> screenHeight
//                                SheetValue.PartiallyExpanded -> 60.dp
//                                else -> 0.dp
//                            },
//                            animationSpec = tween(durationMillis = 300),
//                            label = "Expand/Collapse Animation"
//                        )
                        val heightInDp = when (sheetState.currentValue) {
                            SheetValue.Expanded -> screenHeight
                            SheetValue.PartiallyExpanded -> 60.dp
                            else -> 0.dp
                        }
                        PlayerView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(heightInDp),
                            playerState = playerState!!,
                            sheetValue = sheetState.currentValue,
                            onExpandCallback = {
                                coroutineScope.launch {
                                    sheetState.expand()
                                }
                            },
                            onCollapseCallback = {
                                coroutineScope.launch {
                                    sheetState.partialExpand()
                                }
                            }
                        )
                    }
                },
                scaffoldState = sheetScaffoldState,
                sheetPeekHeight = if (playerState != null) 60.dp else 0.dp,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Radio Stations")
                        },
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    RadioStationList(
                        modifier = Modifier.padding(paddingValues),
                        items = items,
                        onItemClick = { item ->
                            coroutineScope.launch {
                                viewModel.onMediaChanged(item)
                                if (sheetState.isVisible.not())
                                    sheetState.partialExpand()
                            }
                        }
                    )
                }
            }

        }
        else -> {

        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}