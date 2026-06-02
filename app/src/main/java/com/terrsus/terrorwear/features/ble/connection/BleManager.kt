package com.terrsus.terrorwear.features.ble.connection

import android.annotation.SuppressLint
import com.terrsus.terrorwear.features.ble.model.BleDevice
import com.terrsus.terrorwear.features.ble.model.BleState
import com.terrsus.terrorwear.features.ble.scanner.BleScannerImpl
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@SuppressLint("MissingPermission")
class BleManager(
    private val client: BleClient
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _state = MutableStateFlow<BleState>(BleState.Idle)
    val state: StateFlow<BleState> = _state

    private val _devices = MutableStateFlow<List<BleDevice>>(emptyList())
    val devices: StateFlow<List<BleDevice>> = _devices

    fun start() {
        _state.value = BleState.Scanning

        @OptIn(FlowPreview::class)
        client.scanResults
            .sample(250)
            .onEach { devices ->
                _devices.value = devices
                _state.value = BleState.Devices(devices)
            }
            .catch { e -> _state.value = BleState.Error(e.message ?: "Unknown error") }
            .launchIn(scope)

        client.startScan()
    }

    fun stop() {
        client.stopScan()
        scope.coroutineContext.cancelChildren()
        _state.value = BleState.Idle
        _devices.value = emptyList()
    }
}
