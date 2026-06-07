package com.terrsus.terrorwear.features.ble.domain.usecase

import com.terrsus.terrorwear.features.ble.data.BleRepository
import com.terrsus.terrorwear.features.ble.domain.model.BleDevice
import kotlinx.coroutines.flow.StateFlow

class
ObserveBleDevicesUseCase(
    private val repo: BleRepository
) {
    operator fun invoke(): StateFlow<List<BleDevice>> = repo.devices
}
