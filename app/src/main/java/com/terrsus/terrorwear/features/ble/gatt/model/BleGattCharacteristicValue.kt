package com.terrsus.terrorwear.features.ble.gatt.model

import java.util.UUID

data class BleGattCharacteristicValue(
    val serviceUuid: UUID,
    val characteristicUuid: UUID,
    val value: ByteArray
)