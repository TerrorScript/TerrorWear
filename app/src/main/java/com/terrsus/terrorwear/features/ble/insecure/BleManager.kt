package com.terrsus.terrorwear.features.ble.insecure

import android.annotation.SuppressLint
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

@SuppressLint("MissingPermission")
class BleManager(
    private val client: BleScannerClient
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val isScanning = client.isScanning

    private val _devices = MutableStateFlow<List<BleDevice>>(emptyList())
    val devices: StateFlow<List<BleDevice>> = _devices

    @OptIn(FlowPreview::class)
    fun start() {
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