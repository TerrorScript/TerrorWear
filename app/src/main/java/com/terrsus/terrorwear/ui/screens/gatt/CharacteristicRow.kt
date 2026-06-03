package com.terrsus.terrorwear.ui.screens.gatt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.terrsus.terrorwear.features.ble.gatt.model.BleGattCharacteristic

@Composable
fun CharacteristicRow(
    characteristic: BleGattCharacteristic,
    onRead: () -> Unit,
    onWrite: () -> Unit,
    onNotify: () -> Unit
) {
    Column {
        Text("Char: ${characteristic.uuid}")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (characteristic.properties and 0x02 != 0) {
                Button(onClick = onRead) { Text("Read") }
            }
            if (characteristic.properties and 0x08 != 0) {
                Button(onClick = onNotify) { Text("Notify") }
            }
            if (characteristic.properties and 0x04 != 0) {
                Button(onClick = onWrite) { Text("Write") }
            }
        }
    }
}
