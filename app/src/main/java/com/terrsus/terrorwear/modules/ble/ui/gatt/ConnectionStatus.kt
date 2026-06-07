package com.terrsus.terrorwear.modules.ble.ui.gatt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.terrsus.terrorwear.features.ble.common.model.BleGattConnectionState

@Composable
fun ConnectionStatus(state: BleGattConnectionState) {
    val color = when (state) {
        BleGattConnectionState.Connected -> Color(0xFF4CAF50)
        BleGattConnectionState.Connecting -> Color(0xFFFFC107)
        BleGattConnectionState.Disconnected -> Color(0xFFF44336)
        is BleGattConnectionState.Failed -> Color.Gray
    }

    val label = when (state) {
        BleGattConnectionState.Connected -> "Connected"
        BleGattConnectionState.Connecting -> "Connecting"
        BleGattConnectionState.Disconnected -> "Disconnected"
        is BleGattConnectionState.Failed -> "Failed"
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.caption1
        )
    }
}