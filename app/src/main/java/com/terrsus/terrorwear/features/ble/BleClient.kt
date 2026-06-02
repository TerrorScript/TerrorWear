package com.terrsus.terrorwear.features.ble

import android.content.Context

class BleClient(
    context: Context
) {

    private val scanner = BleScanner(context)

    val isScanning = scanner.isScanning
    val scanResults = scanner.scanResults

    fun startScan() = scanner.startScan()
    fun stopScan() = scanner.stopScan()
}
