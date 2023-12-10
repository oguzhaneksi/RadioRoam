package com.radioroam.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.radioroam.android.domain.model.RadioStation
import com.radioroam.android.ui.components.loading.LoadingNextPageItem
import com.radioroam.android.ui.viewmodel.ScreenState

@Composable
fun RadioStationPaginatedList(
    modifier: Modifier = Modifier,
    state: ScreenState,
    onNextPage: () -> Unit,
    onItemClick: (Int) -> Unit = {},
    onFavClick: (RadioStation) -> Unit = {}
) {
    val scrollState = rememberLazyListState()
    LazyColumn(
        modifier = modifier,
        state = scrollState
    ) {
        items(
            count = state.items.size
        ) { index ->
            LaunchedEffect(scrollState) {
                if (index >= state.items.size - 1 && !state.endReached && !state.isLoading) {
                    onNextPage()
                }
            }
            state.items[index].let { item ->
                RadioStationRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp)
                        .background(Color.White)
                        .padding(16.dp)
                        .clickable {
                            onItemClick(index)
                        },
                    item = item,
                    isFavorite = item.isFavorite,
                    onFavClick = onFavClick
                )
            }
        }
        item {
            if (state.isLoading) {
                LoadingNextPageItem()
            }
        }
    }
}