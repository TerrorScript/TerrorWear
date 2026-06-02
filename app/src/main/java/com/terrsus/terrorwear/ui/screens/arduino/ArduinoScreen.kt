package com.terrsus.terrorwear.ui.screens.arduino

import android.bluetooth.le.ScanResult
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.wear.compose.material.*
import com.terrsus.terrorwear.ui.components.BottomStatusPopup
import com.terrsus.terrorwear.viewmodel.ArduinoViewModel
import kotlinx.coroutines.delay

@Composable
fun ArduinoScreen(
    viewModel: ArduinoViewModel
) {
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current
    val statusMessage by viewModel.statusMessage.collectAsState()

    val view = LocalView.current

    SideEffect {
        view.keepScreenOn = true
    }

    val scanning by viewModel.isScanning.collectAsState()
    val results by viewModel.scanResults.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
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
                Chip(label = { Text(if (scanning) "Stop Scan" else "Start Scan") }, onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (scanning) {
                        viewModel.endScan()
                        viewModel.showStatus("Scan stopped")
                    } else {
                        viewModel.beginScan()
                        viewModel.showStatus("Scan started")
                    }
                })
            }

            items(results.size) { i ->
                val device: ScanResult = results[i]
                Chip(
                    label = { Text(device.device?.name ?: "Unknown") },
                    secondaryLabel = { Text(device.device.address ?: "No address") },
                    onClick = {
                        if (device == viewModel.selectedDevice) {
                            haptics.performHapticFeedback(HapticFeedbackType.Reject)
                            viewModel.showStatus("Device already selected.")
                            return@Chip
                        }
                        viewModel.selectDevice(device)
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.showStatus("Selected\n${device.device?.name ?: "Unknown"}")
                    })
            }
        }

        var lastMessage by remember { mutableStateOf("") }

        // Update lastMessage only when a new non-empty message arrives
        if (statusMessage.isNotEmpty()) lastMessage = statusMessage

        AnimatedVisibility(
            visible = statusMessage.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            BottomStatusPopup(lastMessage)
        }

        LaunchedEffect(statusMessage) {
            if (statusMessage.isNotEmpty()) {
                delay(1500)
                viewModel.showStatus("")
            }
    }
    }
}
