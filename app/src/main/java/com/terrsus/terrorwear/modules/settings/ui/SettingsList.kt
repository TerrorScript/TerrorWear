package com.terrsus.terrorwear.modules.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

/**
 * Container for grouping multiple settings items into a vertical list.
 *
 * This component does not impose scrolling behavior; callers may wrap it
 * in a LazyColumn or other scrollable container if needed.
 */
@Composable
fun SettingsList(
    content: @Composable () -> Unit
) {
    Column {
        content()
    }
}
