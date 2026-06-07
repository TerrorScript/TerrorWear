package com.terrsus.terrorwear.features.ble.common.model

import java.util.UUID

data class BleGattCharacteristicValue(
    val serviceUuid: UUID,
    val characteristicUuid: UUID,
    val value: ByteArray
)