package com.terrsus.terrorwear.features.ble.scanner

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import com.terrsus.terrorwear.features.ble.model.BleDevice
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class BleScannerImpl(context: Context): BleScanner {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
    private val adapter = bluetoothManager?.adapter
    private val scanner = adapter?.bluetoothLeScanner

    private val devices = LinkedHashMap<String, ScanResult>()
    private val _pendingUpdates = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private val _isScanning = MutableStateFlow(false)
    override val isScanning: StateFlow<Boolean> = _isScanning

    @OptIn(FlowPreview::class)
    @SuppressLint("MissingPermission")
    override val scanResults: StateFlow<List<BleDevice>> =
        _pendingUpdates
            .sample(300)
            .map {
                devices.values.map { result ->
                    BleDevice(
                        address = result.device.address,
                        name = result.device.name,
                        rssi = result.rssi
                    )
                }
            }
            .stateIn(scope, SharingStarted.Eagerly, emptyList())

    private val callback = object : ScanCallback() {
        override fun onScanResult(type: Int, result: ScanResult) {
            Log.d("BLE", "Scan result received")
            devices[result.device.address] = result
            _pendingUpdates.tryEmit(Unit)
        }
    }

    @SuppressLint("MissingPermission")
    override fun startScan() {
        if (adapter?.isEnabled != true) return
        if (_isScanning.value) return

        devices.clear()
        _isScanning.value = true
        scanner?.startScan(callback)
    }

    @SuppressLint("MissingPermission")
    override fun stopScan() {
        if (!_isScanning.value) return
        _isScanning.value = false
        scanner?.stopScan(callback)
    }

    init {
        Log.d("BleScanner", "BleScannerImpl created")
    }
}