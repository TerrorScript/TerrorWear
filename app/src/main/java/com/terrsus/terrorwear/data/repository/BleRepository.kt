package com.terrsus.terrorwear.data.repository

import com.terrsus.terrorwear.features.ble.BleDevice
import kotlinx.coroutines.flow.StateFlow

interface BleRepository {
    val isScanning: StateFlow<Boolean>
    val scanResults: StateFlow<List<BleDevice>>

    fun startScan()
    fun stopScan()
}
