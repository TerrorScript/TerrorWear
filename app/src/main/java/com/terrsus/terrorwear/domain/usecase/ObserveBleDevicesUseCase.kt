package com.terrsus.terrorwear.domain.usecase

import com.terrsus.terrorwear.data.ble.BleRepository
import com.terrsus.terrorwear.domain.ble.model.BleDevice
import kotlinx.coroutines.flow.StateFlow

class ObserveBleDevicesUseCase(
    private val repo: BleRepository
) {
    operator fun invoke(): StateFlow<List<BleDevice>> = repo.devices
}
