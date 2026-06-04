package com.terrsus.terrorwear.ui.screens.gatt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.terrsus.terrorwear.features.ble.gatt.BleGattConnectionState
import com.terrsus.terrorwear.features.ble.gatt.model.BleGattService
import com.terrsus.terrorwear.viewmodel.GattViewModel

@Composable
fun GattScreen(viewModel: GattViewModel) {
    val connection by viewModel.connectionState.collectAsState()
    val services by viewModel.services.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(Modifier.height(8.dp))
            ConnectionStatus(connection)
            Spacer(Modifier.height(8.dp))
        }

        items(services) { service ->
            ServiceCard(
                service = service,
                onRead = { ch -> viewModel.read(service.uuid, ch.uuid) },
                onWrite = { ch -> viewModel.write(service.uuid, ch.uuid, "HELLO".encodeToByteArray()) },
                onNotify = { ch -> viewModel.enableNotifications(service.uuid, ch.uuid) }
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}