package com.terrsus.terrorwear.domain.usecase

import com.terrsus.terrorwear.features.ble.BleManager
import kotlinx.coroutines.flow.StateFlow

class ObserveBleDevicesUseCase(
    private val bleManager: BleManager
) {
    operator fun invoke(): StateFlow<List<com.terrsus.terrorwear.features.ble.BleDevice>> =
        bleManager.devices
}
