package com.terrsus.terrorwear.features.ble.gatt

import com.terrsus.terrorwear.features.ble.gatt.model.BleGattCharacteristicValue
import com.terrsus.terrorwear.features.ble.gatt.model.BleGattService
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface BleGattClient {

    fun connect(address: String)
    fun disconnect(address: String)

    fun connectionState(address: String): Flow<BleGattConnectionState>

    fun services(address: String): Flow<List<BleGattService>>

    fun notifications(address: String): Flow<BleGattCharacteristicValue>

    fun read(address: String, service: UUID, characteristic: UUID): Flow<BleGattCharacteristicValue>

    fun write(address: String, service: UUID, characteristic: UUID, data: ByteArray)

    fun enableNotifications(address: String, service: UUID, characteristic: UUID)
}
