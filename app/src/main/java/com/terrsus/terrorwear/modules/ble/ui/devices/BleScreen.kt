package com.terrsus.terrorwear.ui.screens.ble

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.terrsus.terrorwear.ui.components.BottomStatusPopup
import com.terrsus.terrorwear.viewmodel.ble.BleViewModel
import kotlinx.coroutines.delay

@Composable
fun BleScreen(
    navController: NavHostController,
    viewModel: BleViewModel
) {
    val haptics = LocalHapticFeedback.current
    val view = LocalView.current

    SideEffect { view.keepScreenOn = true }

    val scanning by viewModel.isScanning.collectAsState()
    val results by viewModel.filteredResults.collectAsState()
    val deviceCount by viewModel.deviceCount.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()
    val selectedDevice by viewModel.selectedDevice.collectAsState()
    val navTarget by viewModel.navigateToGatt.collectAsState()

    LaunchedEffect(navTarget) {
        navTarget?.let { device ->
            navController.navigate("gatt/${device.address}")
            viewModel.clearGattNavigation() // reset
        }
    }
    var lastMessage by remember { mutableStateOf("") }
    if (statusMessage.isNotEmpty()) lastMessage = statusMessage

//    TimeText()

    Box(Modifier.fillMaxSize()) {
        BleContent(
            scanning = scanning,
            results = results,
            deviceCount = deviceCount,
            searchQuery = searchQuery,
            onSearchChange = { viewModel.updateSearchQuery(it) },
            onToggleScan = {
                Log.i("BLE", "Toggle Scan")
                if (scanning) {
                    Log.d("BLE", "Stop Scan")
                    viewModel.endScan()
                    viewModel.showStatus("Scan stopped")
                } else {
                    Log.d("BLE", "Start Scan")
                    viewModel.beginScan()
                    viewModel.showStatus("Scan started")
                }
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            },
            onSelectDevice = { device ->
                if (device == viewModel.selectedDevice) {
                    haptics.performHapticFeedback(HapticFeedbackType.Reject)
                    viewModel.showStatus("Device already selected.")
                } else {
                    viewModel.selectDevice(device)
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.showStatus("Selected\n${device.name ?: "Unknown"}")
                    viewModel.navigateToGatt(device)
                }
            }
        )

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
