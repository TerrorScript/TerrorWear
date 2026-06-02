package com.terrsus.terrorwear.ui.screens.arduino

import androidx.compose.runtime.*
import androidx.wear.compose.material.*
import com.terrsus.terrorwear.viewmodel.ArduinoViewModel

@Composable
fun ArduinoScreen(
    viewModel: ArduinoViewModel
) {
    val scanning by viewModel.isScanning.collectAsState()
    val results by viewModel.scanResults.collectAsState()

    ScalingLazyColumn {
        item {
            Text(if (scanning) "Scanning…" else "Idle")
        }

        item {
            Chip(
                label = { Text(if (scanning) "Stop Scan" else "Start Scan") },
                onClick = {
                    if (scanning) viewModel.endScan()
                    else viewModel.beginScan()
                }
            )
        }

        items(results.size) { i ->
            val device = results[i]
            Text(device.device?.name ?: "Unknown")
        }
    }
}
