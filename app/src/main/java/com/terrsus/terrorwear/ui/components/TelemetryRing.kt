package com.terrsus.terrorwear.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

/**
 * Single circular telemetry ring with an icon in the center and a label below.
 *
 * Intended for high-level summary metrics. The ring color reflects the value
 * severity (green → yellow → orange → red).
 */
@Composable
fun TelemetryRing(
    label: String,
    value: Float,
    iconRes: Int,
    modifier: Modifier = Modifier
) {
    val clamped = value.coerceIn(0f, 1f)
    val color = GetTelemetryColor(clamped)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(64.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(42.dp)
        ) {
            CircularProgressIndicator(
                progress = clamped,
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 4.dp,
                indicatorColor = color,
                trackColor = MaterialTheme.colors.onBackground.copy(alpha = 0.2f)
            )
            Icon(
                painter = painterResource(iconRes),
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.caption2
        )
    }
}