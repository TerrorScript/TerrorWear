package com.terrsus.terrorwear.features.ble.insecure

import android.annotation.SuppressLint
import android.util.Log
import com.terrsus.terrorwear.features.ble.domain.model.BleDevice
import com.terrsus.terrorwear.features.ble.insecure.scanner.BleScannerClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample

private const val LogTag = "TW/BLE/Manager"

@SuppressLint("MissingPermission")
class BleManager(
    private val client: BleScannerClient
) {
    init {
        Log.d(LogTag, "init")
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val isScanning = client.isScanning

    private val _devices = MutableStateFlow<List<BleDevice>>(emptyList())
    val devices: StateFlow<List<BleDevice>> = _devices

    @OptIn(FlowPreview::class)
    fun start() {
        Log.d(LogTag, "Start scan")

        client.scanResults
            .sample(250)
            .onEach { devices -> _devices.value = devices }
            .launchIn(scope)

        client.startScan()
    }

    fun stop() {
        client.stopScan()
        scope.coroutineContext.cancelChildren()
    }
}