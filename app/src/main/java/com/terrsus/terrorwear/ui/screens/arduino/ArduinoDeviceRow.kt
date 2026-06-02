package com.terrsus.terrorwear.ui.screens.arduino

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text
import com.terrsus.terrorwear.features.ble.model.BleDevice

@Composable
fun ArduinoDeviceRow(
    device: BleDevice,
    isSelected: Boolean,
    onSelect: (BleDevice) -> Unit
) {
    Chip(
        label = { Text(device.name ?: "Unknown") },
        secondaryLabel = { Text(device.address) },
        onClick = { onSelect(device) }
    )
}
