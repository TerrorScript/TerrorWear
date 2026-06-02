package com.terrsus.terrorwear.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terrsus.terrorwear.AppContainer
import com.terrsus.terrorwear.features.ble.BleDevice
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ArduinoViewModel : ViewModel() {

    private val ble = AppContainer.bleManager

    val isScanning: StateFlow<Boolean> = ble.state
        .map { it is com.terrsus.terrorwear.features.ble.BleState.Scanning }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val scanResults = ble.devices

    private val _selectedDevice = MutableStateFlow<BleDevice?>(null)
    val selectedDevice: StateFlow<BleDevice?> = _selectedDevice

    fun selectDevice(device: BleDevice) {
        _selectedDevice.value = device
    }

    fun beginScan() = ble.start()
    fun endScan() = ble.stop()

    private val _statusMessage = MutableStateFlow("")
    val statusMessage: StateFlow<String> = _statusMessage

    fun showStatus(msg: String) {
        _statusMessage.value = msg
    }
}