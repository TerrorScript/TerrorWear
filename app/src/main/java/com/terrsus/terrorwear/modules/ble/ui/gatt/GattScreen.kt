package com.terrsus.terrorwear.modules.ble.ui.gatt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.terrsus.terrorwear.ui.components.ServiceCard
import com.terrsus.terrorwear.viewmodel.ble.GattViewModel

@Composable
fun GattScreen(viewModel: GattViewModel) {
    val connection by viewModel.connectionState.collectAsState()
    val services by viewModel.services.collectAsState()

    val scrollState = rememberLazyListState()

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scrollState) }
    ) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(Modifier.height(16.dp))
                ConnectionStatus(connection)
                Spacer(Modifier.height(8.dp))
            }

            items(services) { service ->
                ServiceCard(
                    service = service,
                    onRead = { bleGattCharacteristic ->
                        viewModel.read(service.uuid, bleGattCharacteristic.uuid)
                    },
                    onWrite = { bleGattCharacteristic ->
                        viewModel.write(
                            service = service.uuid,
                            characteristic = bleGattCharacteristic.uuid,
                            data = "HELLO".encodeToByteArray()
                        )
                    },
                    onNotify = { bleGattCharacteristic ->
                        viewModel.enableNotifications(service.uuid, bleGattCharacteristic.uuid)
                    }
                )

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}