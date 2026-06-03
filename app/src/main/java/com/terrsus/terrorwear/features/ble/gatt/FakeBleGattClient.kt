package com.terrsus.terrorwear.features.ble.gatt

import android.content.Context
import com.terrsus.terrorwear.features.ble.gatt.model.BleGattService
import com.terrsus.terrorwear.features.ble.gatt.model.BleGattCharacteristic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class FakeBleGattClient(
    private val context: Context
) : BleGattClient {

    private val connection = MutableStateFlow<BleGattConnectionState>(
        BleGattConnectionState.Disconnected
    )

    private val servicesFlow = MutableStateFlow<List<BleGattService>>(emptyList())
    private val notificationsFlow = MutableStateFlow(ByteArray(0))

    override fun connect(address: String) {
        connection.value = BleGattConnectionState.Connected

        servicesFlow.value = listOf(
            BleGattService(
                uuid = UUID.randomUUID(),
                characteristics = listOf(
                    BleGattCharacteristic(UUID.randomUUID(), properties = 0x02), // Read
                    BleGattCharacteristic(UUID.randomUUID(), properties = 0x08)  // Notify
                )
            )
        )
    }

    override fun disconnect(address: String) {
        connection.value = BleGattConnectionState.Disconnected
    }

    override fun connectionState(address: String): Flow<BleGattConnectionState> = connection

    override fun services(address: String): Flow<List<BleGattService>> = servicesFlow

    override fun notifications(address: String): Flow<ByteArray> = notificationsFlow

    override fun read(address: String, service: UUID, characteristic: UUID): Flow<ByteArray> {
        notificationsFlow.value = "READ_OK".encodeToByteArray()
        return notificationsFlow
    }

    override fun write(address: String, service: UUID, characteristic: UUID, data: ByteArray) {
        notificationsFlow.value = data
    }

    override fun enableNotifications(address: String, service: UUID, characteristic: UUID) {
        notificationsFlow.value = "NOTIFY_ENABLED".encodeToByteArray()
    }
}
