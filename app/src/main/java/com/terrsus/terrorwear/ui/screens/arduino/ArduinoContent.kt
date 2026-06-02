package com.terrsus.terrorwear.ui.screens.arduino

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.terrsus.terrorwear.features.ble.model.BleDevice

@Composable
fun ArduinoContent(
    scanning: Boolean,
    results: List<BleDevice>,
    selectedDevice: BleDevice?,
    onToggleScan: () -> Unit,
    onSelectDevice: (BleDevice) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedVisibility(visible = scanning) {
                CircularProgressIndicator()
            }

            val hasResults = results.isNotEmpty()
            AnimatedVisibility(visible = !hasResults) {
                NoDevicesPlaceholder(scanning)
            }

            DeviceList(scanning, results, selectedDevice, onToggleScan, onSelectDevice)
        }
    }
}

@Composable
fun DeviceList(
    scanning: Boolean,
    results: List<BleDevice>,
    selectedDevice: BleDevice?,
    onToggleScan: () -> Unit,
    onSelectDevice: (BleDevice) -> Unit
) {
    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {

        item {
            Text(
                text = if (scanning) "Scanning…" else "Idle",
                style = MaterialTheme.typography.title3
            )
        }

        item {
            Chip(
                label = { Text(if (scanning) "Stop Scan" else "Start Scan") },
                onClick = onToggleScan
            )
        }

        items(
            items = results,
            key = { it.address }
        ) { device ->
            ArduinoDeviceRow(
                device = device,
                isSelected = device == selectedDevice,
                onSelect = onSelectDevice
            )
        }
    }
}

@Composable
fun NoDevicesPlaceholder(scanning: Boolean) {
    Text(
        text = if (scanning) "No devices found yet..." else "No devices",
        style = MaterialTheme.typography.body2
    )
}