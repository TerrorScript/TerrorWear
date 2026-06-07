package com.terrsus.terrorwear.features.ble.data

import com.terrsus.terrorwear.features.ble.domain.model.BleDevice
import kotlinx.coroutines.flow.StateFlow

interface BleRepository {
    val isScanning: StateFlow<Boolean>
    val devices: StateFlow<List<BleDevice>>

    fun startScan()
    fun stopScan()
}