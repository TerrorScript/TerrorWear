package com.terrsus.terrorwear.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.MaterialTheme

@Composable
fun GetTelemetryColor(value: Float): Color {
    return when {
        value < 0.5f -> MaterialTheme.colors.primary
        value < 0.75f -> MaterialTheme.colors.secondary
        value < 0.9f -> MaterialTheme.colors.error.copy(alpha = 0.7f)
        else -> MaterialTheme.colors.error
    }
}