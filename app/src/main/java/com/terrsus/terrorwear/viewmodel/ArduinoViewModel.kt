package com.terrsus.terrorwear.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terrsus.terrorwear.AppContainer
import com.terrsus.terrorwear.domain.usecase.*
import com.terrsus.terrorwear.features.ble.model.BleDevice
import com.terrsus.terrorwear.features.ble.model.BleState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

class ArduinoViewModel : ViewModel() {

    private val observeDevices = AppContainer.observeBleDevicesUseCase
    private val observeState = AppContainer.observeBleStateUseCase
    private val startScan = AppContainer.startBleScanUseCase
    private val stopScan = AppContainer.stopBleScanUseCase

    @OptIn(FlowPreview::class)
    val scanResults = observeDevices()
        .map { list -> list.distinctBy { it.address } }
        .sample(250) // emit at most every 250ms
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val isScanning: StateFlow<Boolean> =
        observeState()
            .map { state -> state is BleState.Scanning }
            .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _selectedDevice = MutableStateFlow<BleDevice?>(null)
    val selectedDevice: StateFlow<BleDevice?> = _selectedDevice

    fun selectDevice(device: BleDevice) {
        _selectedDevice.value = device
    }

    fun beginScan() = startScan()
    fun endScan() = stopScan()

    private val _statusMessage = MutableStateFlow("")
    val statusMessage: StateFlow<String> = _statusMessage

    private val _navigateToGatt = MutableStateFlow<BleDevice?>(null)
    val navigateToGatt: StateFlow<BleDevice?> = _navigateToGatt

    fun navigateToGatt(device: BleDevice) {
        _navigateToGatt.value = device
    }
    fun clearGattNavigation() {
        _navigateToGatt.value = null
    }

    fun showStatus(msg: String) {
        _statusMessage.value = msg
    }
}
