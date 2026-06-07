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
 * Placeholder for network-related laptop tools.
 *
 * Intended for future integration with host networking utilities:
 * - pinging the laptop
 * - Wi‑Fi signal strength
 * - LAN device discovery
 */
@Composable
fun NetworkToolsPage() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        InfoIsland {
            Text("Network Tools", style = MaterialTheme.typography.caption1)
            Spacer(Modifier.height(6.dp))
            Text("Ping laptop (stub)")
            Text("Wi‑Fi strength (stub)")
            Text("LAN device list (stub)")
        }
    }
}