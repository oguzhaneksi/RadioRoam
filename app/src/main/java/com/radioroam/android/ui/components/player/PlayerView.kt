package com.radioroam.android.ui.components.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.radioroam.android.ui.state.PlayerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerView(
    modifier: Modifier = Modifier,
    playerState: PlayerState,
    sheetValue: SheetValue,
    onExpandCallback: () -> Unit,
    onCollapseCallback: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        if (sheetValue == SheetValue.Expanded) {
            ExpandedPlayerView(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface),
                playerState = playerState,
                onCollapseTap = onCollapseCallback,
                onMenuTap = {}
            )
        }
        else if (sheetValue == SheetValue.PartiallyExpanded) {
            CompactPlayerView(
                modifier = Modifier
                    .clickable {
                        onExpandCallback()
                    },
                playerState = playerState
            )
        }
    }
}