package com.terrsus.terrorwear.domain.usecase

import com.terrsus.terrorwear.data.repository.BleRepository
import com.terrsus.terrorwear.features.ble.connection.BleManager
import com.terrsus.terrorwear.features.ble.model.BleDevice
import kotlinx.coroutines.flow.StateFlow

class ObserveBleDevicesUseCase(
    private val repo: BleRepository
) {
    operator fun invoke(): StateFlow<List<BleDevice>> = repo.devices
}
