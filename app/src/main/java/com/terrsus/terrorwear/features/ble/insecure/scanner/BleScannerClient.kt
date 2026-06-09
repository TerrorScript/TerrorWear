package com.terrsus.terrorwear.features.ble.insecure.scanner

import android.content.Context
import android.util.Log
import com.terrsus.terrorwear.util.DeviceUtils

private const val LogTag = "TW/BLE/ScannerClient"

class BleScannerClient(
    context: Context
) {
    init {
        Log.d(LogTag, "init")
        Log.d(LogTag, "isEmulator = ${DeviceUtils.isEmulator}")
    }
    private val scanner: BleScanner =
        if (DeviceUtils.isEmulator) BleScannerFake()
        else BleScannerImpl(context)

    val isScanning = scanner.isScanning
    val scanResults = scanner.scanResults

    fun startScan() = scanner.startScan()
    fun stopScan() = scanner.stopScan()
}