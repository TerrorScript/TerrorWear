package com.terrsus.terrorwear.features.ble.data

import com.terrsus.terrorwear.features.ble.insecure.BleManager
import com.terrsus.terrorwear.features.ble.domain.model.BleDevice
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