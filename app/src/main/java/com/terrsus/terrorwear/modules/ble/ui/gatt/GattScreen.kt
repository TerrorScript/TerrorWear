package com.terrsus.terrorwear.modules.ble.ui.gatt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.terrsus.terrorwear.ui.components.ServiceCard
import com.terrsus.terrorwear.viewmodel.ble.GattViewModel

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
                onWrite = { ch ->
                    viewModel.write(
                        service.uuid,
                        ch.uuid,
                        "HELLO".encodeToByteArray()
                    )
                },
                onNotify = { ch -> viewModel.enableNotifications(service.uuid, ch.uuid) }
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}