package com.terrsus.terrorwear.modules.tools.programassist.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.terrsus.terrorwear.ui.components.InfoIsland

/**
 * Placeholder for system-level laptop tools.
 *
 * Intended for future integration with host controls such as:
 * - volume and mute
 * - brightness
 * - media playback keys
 */
@Composable
fun SystemToolsPage() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        InfoIsland {
            Text("System Tools", style = MaterialTheme.typography.caption1)
            Spacer(Modifier.height(6.dp))
            Text("Volume control (stub)")
            Text("Brightness control (stub)")
            Text("Media keys (stub)")
        }
    }
}