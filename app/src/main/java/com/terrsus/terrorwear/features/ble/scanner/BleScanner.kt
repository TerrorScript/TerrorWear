package com.terrsus.terrorwear.features.ble.scanner

import com.terrsus.terrorwear.features.ble.model.BleDevice
import kotlinx.coroutines.flow.StateFlow

interface BleScanner {
    val isScanning: StateFlow<Boolean>
    val scanResults: StateFlow<List<BleDevice>>

    fun startScan()
    fun stopScan()
}
