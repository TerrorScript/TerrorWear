package com.terrsus.terrorwear.features.ble.common.util

import com.terrsus.terrorwear.features.ble.common.model.BleGattCharacteristicValue
import com.terrsus.terrorwear.features.ble.common.model.BleGattService

fun List<BleGattService>.updateValue(update: BleGattCharacteristicValue): List<BleGattService> {
    return map { service ->
        if (service.uuid != update.serviceUuid) return@map service

        service.copy(
            characteristics = service.characteristics.map { ch ->
                if (ch.uuid == update.characteristicUuid) {
                    ch.copy(value = update.value)
                } else ch
            }
        )
    }
}