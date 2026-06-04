package com.terrsus.terrorwear.domain.usecase

import com.terrsus.terrorwear.data.repository.BleRepository
import kotlinx.coroutines.flow.StateFlow

class ObserveBleScanningUseCase(
    private val repo: BleRepository
) {
    operator fun invoke(): StateFlow<Boolean> = repo.isScanning
}