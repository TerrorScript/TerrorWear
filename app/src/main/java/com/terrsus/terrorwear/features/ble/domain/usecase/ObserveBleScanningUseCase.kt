package com.terrsus.terrorwear.features.ble.domain.usecase

import com.terrsus.terrorwear.features.ble.data.BleRepository
import kotlinx.coroutines.flow.StateFlow

class ObserveBleScanningUseCase(
    private val repo: BleRepository
) {
    operator fun invoke(): StateFlow<Boolean> = repo.isScanning
}