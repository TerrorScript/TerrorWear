package com.terrsus.terrorwear.data.repository

import com.terrsus.terrorwear.features.ble.connection.BleManager
import com.terrsus.terrorwear.features.ble.model.BleDevice
import kotlinx.coroutines.flow.StateFlow

class BleRepositoryImpl(
    private val bleManager: BleManager
) : BleRepository {

    override val isScanning: StateFlow<Boolean>
        get() = bleManager.isScanning

    override val devices: StateFlow<List<BleDevice>>
        get() = bleManager.devices

    override fun startScan() = bleManager.start()
    override fun stopScan() = bleManager.stop()
}