package com.terrsus.terrorwear.ui.screens.gatt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.terrsus.terrorwear.features.ble.gatt.model.BleGattCharacteristic
import com.terrsus.terrorwear.features.ble.gatt.model.GattProperty
import com.terrsus.terrorwear.features.ble.gatt.model.toGattProperties
import com.terrsus.terrorwear.features.ble.gatt.model.toPrettyString

@Composable
fun CharacteristicRow(
    characteristic: BleGattCharacteristic,
    onRead: () -> Unit,
    onWrite: () -> Unit,
    onNotify: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "UUID: ${characteristic.uuid}",
            style = MaterialTheme.typography.caption1
        )

        val valueText = characteristic.value?.toPrettyString() ?: "<empty>"
        Text(
            "Value: $valueText",
            style = MaterialTheme.typography.caption2
        )

        val gattProperties = characteristic.properties.toGattProperties()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (GattProperty.READ in gattProperties)
                CompactChip(label = { Text("Read") }, onClick = onRead)
            if (GattProperty.NOTIFY in gattProperties)
                CompactChip(label = { Text("Notify") }, onClick = onNotify)
            if (GattProperty.WRITE in gattProperties)
                CompactChip(label = { Text("Write") }, onClick = onWrite)
        }
    }
}
