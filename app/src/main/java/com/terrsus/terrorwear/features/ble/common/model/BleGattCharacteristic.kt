package com.terrsus.terrorwear.features.ble.common.model

import java.util.UUID

data class BleGattCharacteristic(
    val uuid: UUID,
    val properties: Int,
    val value: ByteArray? = null
)