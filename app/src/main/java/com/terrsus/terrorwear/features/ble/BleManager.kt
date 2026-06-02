package com.terrsus.terrorwear.features.ble

import android.annotation.SuppressLint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@SuppressLint("MissingPermission")
class BleManager(
    private val client: BleClient,
    private val scanner: BleScanner
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _state = MutableStateFlow<BleState>(BleState.Idle)
    val state: StateFlow<BleState> = _state

    private val _devices = MutableStateFlow<List<BleDevice>>(emptyList())
    val devices: StateFlow<List<BleDevice>> = _devices

    fun start() {
        _state.value = BleState.Scanning

        @OptIn(FlowPreview::class)
        scanner.scanResults
            .sample(250)
            .onEach { raw ->
                val mapped = raw.map {
                    BleDevice(
                        address = it.device.address,
                        name = it.device.name,
                        rssi = it.rssi
                    )
                }
                _devices.value = mapped
                _state.value = BleState.Devices(mapped)
            }
            .catch { e -> _state.value = BleState.Error(e.message ?: "Unknown error") }
            .launchIn(scope)

        scanner.startScan()
    }

    fun stop() {
        scanner.stopScan()
        scope.coroutineContext.cancelChildren()
        _state.value = BleState.Idle
        _devices.value = emptyList()
    }
}
