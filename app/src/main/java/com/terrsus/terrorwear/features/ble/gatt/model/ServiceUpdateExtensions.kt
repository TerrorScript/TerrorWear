package com.terrsus.terrorwear.features.ble.gatt.model

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