package com.terrsus.terrorwear.features.ble.insecure.scanner

import com.terrsus.terrorwear.features.ble.domain.model.BleDevice
import kotlinx.coroutines.flow.StateFlow

interface BleScanner {
    val isScanning: StateFlow<Boolean>
    val scanResults: StateFlow<List<BleDevice>>

    fun startScan()
    fun stopScan()
}
