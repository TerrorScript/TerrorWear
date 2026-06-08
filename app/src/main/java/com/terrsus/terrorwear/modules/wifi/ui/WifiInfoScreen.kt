package com.terrsus.terrorwear.modules.wifi.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.*
import com.terrsus.terrorwear.R
import com.terrsus.terrorwear.modules.wifi.viewmodel.WifiConnectionViewModel
import com.terrsus.terrorwear.modules.wifi.viewmodel.WifiNetworkInfoViewModel
import com.terrsus.terrorwear.ui.navigation.Route

/**
 * Main information screen for the Wi‑Fi module.
 *
 * Displays connection status, SSID, RSSI, link speed, IP information,
 * and provides navigation to the Tools and Packet Log screens.
 *
 * This screen reacts to StateFlows exposed by [WifiNetworkInfoViewModel].
 */
@Composable
fun WifiInfoScreen(
    navController: NavHostController,
    viewModel: WifiNetworkInfoViewModel,
    connectionViewModel: WifiConnectionViewModel
) {
    val scroll = rememberScalingLazyListState()

    // --- Reactive UI state from ViewModel ---
    val status by connectionViewModel.status.collectAsState()
    val ssid by viewModel.ssid.collectAsState()
    val rssi by viewModel.rssi.collectAsState()
    val linkSpeed by viewModel.linkSpeed.collectAsState()
    val ipAddress by viewModel.ipAddress.collectAsState()
    val security by viewModel.security.collectAsState()

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scroll) }
    ) {
        ScalingLazyColumn(
            state = scroll,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Text(
                    text = "Wi‑Fi Info",
                    style = MaterialTheme.typography.title3
                )
            }

            // --- Connection Status ---
            item {
                InfoRow(
                    iconRes = WifiIcons.Status,
                    label = "Status",
                    value = status.ifEmpty { "Unknown" }
                )
            }

            // --- SSID ---
            item {
                InfoRow(
                    iconRes = WifiIcons.Ssid,
                    label = "SSID",
                    value = ssid
                )
            }

            // --- RSSI ---
            item {
                InfoRow(
                    iconRes = WifiIcons.Rssi,
                    label = "RSSI",
                    value = rssi
                )
            }

            // --- Link Speed ---
            item {
                InfoRow(
                    iconRes = WifiIcons.Speed,
                    label = "Link Speed",
                    value = linkSpeed
                )
            }

            // --- IP Address ---
            item {
                InfoRow(
                    iconRes = WifiIcons.IP,
                    label = "IP Address",
                    value = ipAddress
                )
            }

            // --- Security ---
            item {
                InfoRow(
                    iconRes = R.drawable.baseline_lock_24,
                    label = "Security",
                    value = security
                )
            }

            // --- Navigation Chips ---
            item {
                NavigationChip(
                    label = "Tools",
                    iconRes = WifiIcons.Tools,
                    onClick = { navController.navigate(Route.WifiTools.path) }
                )
            }

            item {
                NavigationChip(
                    label = "Packet Log",
                    iconRes = WifiIcons.List,
                    onClick = { navController.navigate(Route.WifiLogs.path) }
                )
            }
        }
    }
}

/**
 * A reusable row for displaying a labeled Wi‑Fi property with an icon.
 */
@Composable
private fun InfoRow(
    @DrawableRes iconRes: Int,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = label,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(label, style = MaterialTheme.typography.caption2)
            Text(value, style = MaterialTheme.typography.body2)
        }
    }
}

/**
 * A small helper composable for navigation chips to reduce repetition.
 */
@Composable
private fun NavigationChip(
    label: String,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit
) {
    Chip(
        label = { Text(label) },
        icon = {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = label
            )
        },
        onClick = onClick
    )
}