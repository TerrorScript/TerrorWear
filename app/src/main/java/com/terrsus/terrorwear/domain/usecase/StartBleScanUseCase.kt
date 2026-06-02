package com.terrsus.terrorwear.domain.usecase

import com.terrsus.terrorwear.data.repository.BleRepository

class StartBleScanUseCase(
    private val repo: BleRepository
) {
    operator fun invoke() = repo.startScan()
}
