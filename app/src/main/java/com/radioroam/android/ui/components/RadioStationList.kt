package com.radioroam.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.radioroam.android.ui.components.loading.LoadingNextPageItem
import com.radioroam.android.ui.components.loading.PageLoader

@Composable
fun RadioStationList(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<MediaItem>,
    onItemClick: (Int) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            count = items.itemCount
        ) { index ->
            items[index]?.let { item ->
                RadioStationRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp)
                        .background(Color.White)
                        .padding(16.dp)
                        .clickable {
                            onItemClick(index)
                        },
                    item = item
                )
            }
        }
        items.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        PageLoader(
                            modifier = Modifier
                                .fillParentMaxSize()
                        )
                    }
                }
                loadState.refresh is LoadState.Error -> {
                    // TODO implement error states
                }

                loadState.append is LoadState.Loading -> {
                    item {
                        LoadingNextPageItem()
                    }

                }

                loadState.append is LoadState.Error -> {
                    // TODO implement error states
                }
            }
        }
    }
}