package com.terrsus.terrorwear.features.ble.domain.usecase

import com.terrsus.terrorwear.features.ble.data.BleRepository

class StartBleScanUseCase(
    private val repo: BleRepository
) {
    operator fun invoke() = repo.startScan()
}
