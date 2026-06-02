package com.terrsus.terrorwear.domain.usecase

import com.terrsus.terrorwear.features.ble.BleManager
import kotlinx.coroutines.flow.StateFlow

class ObserveBleStateUseCase(
    private val bleManager: BleManager
) {
    operator fun invoke(): StateFlow<com.terrsus.terrorwear.features.ble.BleState> =
        bleManager.state
}
