package com.terrsus.terrorwear.domain.usecase

import com.terrsus.terrorwear.data.ble.BleRepository

class StopBleScanUseCase(
    private val repo: BleRepository
) {
    operator fun invoke() = repo.stopScan()
}
