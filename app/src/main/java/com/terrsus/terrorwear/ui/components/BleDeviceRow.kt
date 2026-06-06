package com.terrsus.terrorwear.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.terrsus.terrorwear.domain.ble.model.BleDevice

@Composable
fun BleDeviceRow(
    device: BleDevice,
    onSelectDevice: (BleDevice) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(80.dp)
                .padding(2.dp)
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(40)
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onSelectDevice(device) }
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val name = device.name ?: "Unknown"
                Text(
                    name,
                    style = MaterialTheme.typography.caption1, //MaterialTheme.typography.title2,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    device.address,
                    style = MaterialTheme.typography.caption2
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "RSSI: ${device.rssi} dBm",
                    style = MaterialTheme.typography.caption2
                )
            }
        }
    }
}