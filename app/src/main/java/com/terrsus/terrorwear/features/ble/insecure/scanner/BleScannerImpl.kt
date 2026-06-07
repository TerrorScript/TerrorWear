package com.terrsus.terrorwear.features.ble.insecure.scanner

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import com.terrsus.terrorwear.features.ble.domain.model.BleDevice
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class BleScannerImpl(context: Context) : BleScanner {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
    private val adapter = bluetoothManager?.adapter
    private val scanner = adapter?.bluetoothLeScanner

    private val devices = LinkedHashMap<String, ScanResult>()
    private val mutex = Mutex()

    private val _isScanning = MutableStateFlow(false)
    override val isScanning: StateFlow<Boolean> = _isScanning

    private val _scanResults = MutableStateFlow<List<BleDevice>>(emptyList())
    override val scanResults: StateFlow<List<BleDevice>> = _scanResults

    // Lock‑free buffer for incoming scan results
    private val incoming = MutableSharedFlow<ScanResult>(extraBufferCapacity = 128)

    private val callback = object : ScanCallback() {
        override fun onScanResult(type: Int, result: ScanResult) {
            Log.d("BLE", "Callback hit: ${result.device.address}")

            // MUST be instant — no locks, no allocations, no suspend
            incoming.tryEmit(result)
        }
    }

    init {
        // Background processor — safe to block here
        scope.launch {
            incoming.collect { result ->
                mutex.withLock {
                    devices[result.device.address] = result

                    _scanResults.value = devices.values.map { r ->
                        BleDevice(
                            address = r.device.address,
                            name = r.device.name,
                            rssi = r.rssi
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun startScan() {
        if (_isScanning.value) return
        if (adapter?.isEnabled != true) return

        scope.launch {
            mutex.withLock { devices.clear() }
            _scanResults.value = emptyList()
        }

        _isScanning.value = true
        scanner?.startScan(callback)
        Log.d("BLE", "Hardware scan started")
    }

    @SuppressLint("MissingPermission")
    override fun stopScan() {
        if (!_isScanning.value) return

        _isScanning.value = false
        scanner?.stopScan(callback)
        Log.d("BLE", "Hardware scan stopped")
    }
}