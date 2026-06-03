package com.terrsus.terrorwear.features.ble.scanner

import android.content.Context
import android.util.Log
import com.terrsus.terrorwear.util.DeviceUtils

class BleScannerClient(
    context: Context
) {

    private val scanner: BleScanner =
        if (DeviceUtils.isEmulator()) FakeBleScanner()
        else BleScannerImpl(context)
    init {
        Log.d("BleClient", "isEmulator = ${DeviceUtils.isEmulator()}")
    }

    val isScanning = scanner.isScanning
    val scanResults = scanner.scanResults

    fun startScan() = scanner.startScan()
    fun stopScan() = scanner.stopScan()
}