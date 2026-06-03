package com.terrsus.terrorwear.ui.screens.gatt

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.Text
import com.terrsus.terrorwear.features.ble.gatt.BleGattConnectionState

@Composable
fun ConnectionStatus(state: BleGattConnectionState) {
    val text = when (state) {
        is BleGattConnectionState.Connecting -> "Connecting…"
        is BleGattConnectionState.Connected -> "Connected"
        is BleGattConnectionState.Disconnected -> "Disconnected"
        is BleGattConnectionState.Failed -> "Failed: ${state.reason}"
    }

    Text(text)
}
