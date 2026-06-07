package com.terrsus.terrorwear.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

/**
 * Horizontal minimalist bar gauge row with icon, label, and percentage.
 *
 * Used for detailed telemetry inside InfoIslands (CPU/GPU load, memory, disk,
 * temperatures). The bar color reflects the current value severity.
 */
@Composable
fun TelemetryBarRow(
    label: String,
    value: Float,              // normalized 0–1
    iconRes: Int,
    displayText: String,       // e.g. "68°C" or "73%"
    modifier: Modifier = Modifier
) {
    val clamped = value.coerceIn(0f, 1f)
    val color = GetTelemetryColor(clamped)

    Column(modifier = modifier.fillMaxWidth(0.9f)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.caption2,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = displayText,   // <-- FIXED
                style = MaterialTheme.typography.caption2
            )
        }
        Spacer(Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(MaterialTheme.colors.onBackground.copy(alpha = 0.2f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(clamped)
                    .height(4.dp)
                    .background(color)
            )
        }
    }
}
