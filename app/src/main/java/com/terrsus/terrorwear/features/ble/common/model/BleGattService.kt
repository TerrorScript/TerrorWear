package com.terrsus.terrorwear.features.ble.common.model

import java.util.UUID

data class BleGattService(
    val uuid: UUID,
    val characteristics: List<BleGattCharacteristic>
)
