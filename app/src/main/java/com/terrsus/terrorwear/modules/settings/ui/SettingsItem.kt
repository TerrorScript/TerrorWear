package com.terrsus.terrorwear.modules.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text

/**
 * Base UI element representing a single settings row.
 *
 * This component is intentionally minimal and is used as a building block
 * for more specialized settings controls such as toggles or selectors.
 */
@Composable
fun SettingsItem(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Column(modifier.padding(12.dp)) {
        Text(text = title)
        content()
    }
}
