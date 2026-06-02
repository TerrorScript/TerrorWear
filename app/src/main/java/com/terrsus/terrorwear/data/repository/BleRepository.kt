package com.terrsus.terrorwear.data.repository

import com.terrsus.terrorwear.features.ble.model.BleDevice
import com.terrsus.terrorwear.features.ble.model.BleState
import kotlinx.coroutines.flow.StateFlow

interface BleRepository {
    val state: StateFlow<BleState>
    val devices: StateFlow<List<BleDevice>>

    fun startScan()
    fun stopScan()
}
