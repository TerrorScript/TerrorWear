package com.terrsus.terrorwear.ui.screens.arduino

import android.bluetooth.le.ScanResult
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.wear.compose.material.*
import com.terrsus.terrorwear.viewmodel.ArduinoViewModel

@Composable
fun ArduinoScreen(
    viewModel: ArduinoViewModel
) {
    val context = LocalContext.current

    val scanning by viewModel.isScanning.collectAsState()
    val results by viewModel.scanResults.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = if (scanning) "Scanning…" else "Idle",
                    style = MaterialTheme.typography.title3
                )
            }

            item {
                Chip(
                    label = { Text(if (scanning) "Stop Scan" else "Start Scan") },
                    onClick = {
                        if (scanning) {
                            viewModel.endScan()
                            Toast.makeText(context, "Scan stopped", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            viewModel.beginScan()
                            Toast.makeText(context, "Scan started", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }

            items(results.size) { i ->
                val device: ScanResult = results[i]
                Chip(
                    label = { Text(device.device?.name ?: "Unknown") },
                    secondaryLabel = { Text(device.device.address ?: "No address") },
                    onClick = {
                        viewModel.selectDevice(device)
                        Toast.makeText(
                            context,
                            "Selected: ${device.device?.name ?: "Unknown"}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }g
                )
            }
        }
    }
}
