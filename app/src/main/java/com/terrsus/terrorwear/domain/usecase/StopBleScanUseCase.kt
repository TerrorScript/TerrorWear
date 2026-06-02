package com.terrsus.terrorwear.domain.usecase

import com.terrsus.terrorwear.data.repository.BleRepository

class StopBleScanUseCase(
    private val repo: BleRepository
) {
    operator fun invoke() = repo.stopScan()
}
