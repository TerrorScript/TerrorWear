package com.terrsus.terrorwear.features.ble.gatt.model

import java.util.UUID

data class BleGattCharacteristic(
    val uuid: UUID,
    val properties: Int
)
