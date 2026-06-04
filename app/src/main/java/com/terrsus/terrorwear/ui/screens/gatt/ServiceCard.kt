package com.terrsus.terrorwear.ui.screens.gatt

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Text
import com.terrsus.terrorwear.features.ble.gatt.model.BleGattCharacteristic
import com.terrsus.terrorwear.features.ble.gatt.model.BleGattService

@Composable
fun ServiceCard(
    service: BleGattService,
    onRead: (BleGattCharacteristic) -> Unit,
    onWrite: (BleGattCharacteristic) -> Unit,
    onNotify: (BleGattCharacteristic) -> Unit
) {
    Card(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(Modifier.padding(8.dp)) {
            Text("Service: ${service.uuid}", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))

            service.characteristics.forEach { ch ->
                CharacteristicRow(
                    characteristic = ch,
                    onRead = { onRead(ch) },
                    onWrite = { onWrite(ch) },
                    onNotify = { onNotify(ch) }
                )
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}
