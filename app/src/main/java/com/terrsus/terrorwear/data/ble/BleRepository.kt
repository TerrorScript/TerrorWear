package com.terrsus.terrorwear.data.ble

import com.terrsus.terrorwear.domain.ble.model.BleDevice
import kotlinx.coroutines.flow.StateFlow

interface BleRepository {
    val isScanning: StateFlow<Boolean>
    val devices: StateFlow<List<BleDevice>>

    fun startScan()
    fun stopScan()
}