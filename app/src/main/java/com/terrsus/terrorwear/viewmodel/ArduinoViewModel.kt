package com.terrsus.terrorwear.viewmodel

import android.bluetooth.le.ScanResult
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terrsus.terrorwear.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArduinoViewModel : ViewModel() {

    private val startScan = AppContainer.startBleScan
    private val stopScan = AppContainer.stopBleScan

    private val _selectedDevice = MutableStateFlow<ScanResult?>(null)
    val selectedDevice: StateFlow<ScanResult?> = _selectedDevice

    val isScanning = AppContainer.bleRepository.isScanning
    val scanResults = AppContainer.bleRepository.scanResults

    fun beginScan() = viewModelScope.launch {
        Log.d("ArduinoVM", "Starting BLE scan")
        startScan()
    }

    fun endScan() = viewModelScope.launch {
        Log.d("ArduinoVM", "Stopping BLE scan")
        stopScan()
    }

    fun selectDevice(device: ScanResult) {
        Log.d("ArduinoVM", "Selected device: ${device.device?.name} (${device.device?.address})")
        _selectedDevice.value = device
    }
}
