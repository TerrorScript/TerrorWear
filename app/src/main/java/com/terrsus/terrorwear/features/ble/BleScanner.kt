package com.terrsus.terrorwear.features.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class BleScanner(context: Context) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
    private val adapter = bluetoothManager?.adapter
    private val scanner = adapter?.bluetoothLeScanner

    private val devices = LinkedHashMap<String, ScanResult>()
    private val _pendingUpdates = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning

    @OptIn(FlowPreview::class)
    val scanResults: StateFlow<List<ScanResult>> =
        _pendingUpdates
            .sample(300)
            .map { devices.values.toList() }
            .stateIn(scope, SharingStarted.Eagerly, emptyList())

    private val callback = object : ScanCallback() {
        override fun onScanResult(type: Int, result: ScanResult) {
            Log.d("BLE", "Scan result received")
            devices[result.device.address] = result
            _pendingUpdates.tryEmit(Unit)
        }
    }

    @SuppressLint("MissingPermission")
    fun startScan() {
        if (adapter?.isEnabled != true) return
        if (_isScanning.value) return

        devices.clear()
        _isScanning.value = true
        scanner?.startScan(callback)
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        if (!_isScanning.value) return
        _isScanning.value = false
        scanner?.stopScan(callback)
    }
}