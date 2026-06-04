package com.terrsus.terrorwear.ui.screens.ble

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.terrsus.terrorwear.features.ble.model.BleDevice

@Composable
fun BleContent(
    scanning: Boolean,
    results: List<BleDevice>,
    deviceCount: Int,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onToggleScan: () -> Unit,
    onSelectDevice: (BleDevice) -> Unit
) {
    val scrollState = rememberScalingLazyListState()

    Scaffold(
        positionIndicator = {
            PositionIndicator(scalingLazyListState = scrollState)
        },
        vignette = {
            Vignette(VignettePosition.TopAndBottom)
        }
    ) {
        ScalingLazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                SearchBarSmall(
                    query = searchQuery,
                    onQueryChange = onSearchChange,
                    deviceCount = deviceCount
                )
            }

            item {
                Chip(
                    label = { Text(if (scanning) "Stop Scan" else "Start Scan") },
                    onClick = onToggleScan
                )
            }

            if (results.isEmpty()) {
                item {
                    NoDevicesPlaceholder(scanning)
                }
            } else {
                items(results.size) { index ->
                    val device = results[index]
                    BleDeviceRow(device, onSelectDevice)
                }
            }
        }
    }
}


@Composable
fun NoDevicesPlaceholder(scanning: Boolean) {
    Text(
        text = if (scanning) "No devices found yet…" else "No devices",
        style = MaterialTheme.typography.body2
    )
}