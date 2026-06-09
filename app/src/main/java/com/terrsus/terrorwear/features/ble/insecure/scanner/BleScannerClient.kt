package com.terrsus.terrorwear.features.ble.insecure.scanner

import android.content.Context
import android.util.Log
import com.terrsus.terrorwear.util.DeviceUtils

class BleScannerClient(
    context: Context
) {
    init {
        Log.d("TW/BleClient", "isEmulator = ${DeviceUtils.isEmulator}")
    }
    private val scanner: BleScanner =
        if (DeviceUtils.isEmulator) FakeBleScanner()
        else BleScannerImpl(context)

    val isScanning = scanner.isScanning
    val scanResults = scanner.scanResults

    fun startScan() = scanner.startScan()
    fun stopScan() = scanner.stopScan()
}