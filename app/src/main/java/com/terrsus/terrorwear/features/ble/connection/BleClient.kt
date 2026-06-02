package com.terrsus.terrorwear.features.ble.connection

import android.content.Context
import android.util.Log
import com.terrsus.terrorwear.features.ble.scanner.BleScanner
import com.terrsus.terrorwear.features.ble.scanner.BleScannerImpl
import com.terrsus.terrorwear.features.ble.scanner.FakeBleScanner
import com.terrsus.terrorwear.util.DeviceUtils

class BleClient(
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
