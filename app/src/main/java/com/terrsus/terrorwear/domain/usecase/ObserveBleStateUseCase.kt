package com.terrsus.terrorwear.domain.usecase

import com.terrsus.terrorwear.data.repository.BleRepository
import com.terrsus.terrorwear.features.ble.connection.BleManager
import com.terrsus.terrorwear.features.ble.model.BleState
import kotlinx.coroutines.flow.StateFlow

class ObserveBleStateUseCase(
    private val repo: BleRepository
) {
    operator fun invoke(): StateFlow<BleState> = repo.state
}