package com.terrsus.terrorwear.features.ble.domain.usecase

import com.terrsus.terrorwear.features.ble.data.BleRepository

class StopBleScanUseCase(
    private val repo: BleRepository
) {
    operator fun invoke() = repo.stopScan()
}
