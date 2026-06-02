package com.terrsus.terrorwear.core.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BleScanner(context: Context) {

    private val bluetoothManager =
        context.getSystemService(BluetoothManager::class.java)

    private val adapter: BluetoothAdapter? =
        bluetoothManager?.adapter

    private val scanner = adapter?.bluetoothLeScanner

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning

    private val _scanResults = MutableStateFlow<List<ScanResult>>(emptyList())
    val scanResults: StateFlow<List<ScanResult>> = _scanResults

    private val callback = object : ScanCallback() {
        override fun onScanResult(type: Int, result: ScanResult) {
            _scanResults.value = _scanResults.value + result
        }
    }

    fun startScan() {
        if (adapter?.isEnabled != true) return
        if (_isScanning.value) return

        _isScanning.value = true
        _scanResults.value = emptyList()

        scanner?.startScan(callback)
    }

    fun stopScan() {
        if (!_isScanning.value) return

        _isScanning.value = false
        scanner?.stopScan(callback)
    }
}
