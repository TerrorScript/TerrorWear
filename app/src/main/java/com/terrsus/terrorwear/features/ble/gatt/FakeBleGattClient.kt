package com.terrsus.terrorwear.features.ble.gatt

import android.content.Context
import com.terrsus.terrorwear.features.ble.gatt.model.BleGattService
import com.terrsus.terrorwear.features.ble.gatt.model.BleGattCharacteristic
import com.terrsus.terrorwear.features.ble.gatt.model.BleGattCharacteristicValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import java.util.UUID


val possibleGattProperties = listOf(
    0x02,                    // Read
    0x04,                    // Write No Response
    0x08,                    // Write
    0x10,                    // Notify
    0x02 or 0x08,            // Read + Write
    0x02 or 0x10,            // Read + Notify
    0x08 or 0x10,            // Write + Notify
    0x02 or 0x08 or 0x10     // Read + Write + Notify
)

class FakeBleGattClient(
    private val context: Context
) : BleGattClient {

    private val connection = MutableStateFlow<BleGattConnectionState>(
        BleGattConnectionState.Disconnected
    )

    private val servicesFlow = MutableStateFlow<List<BleGattService>>(emptyList())
    private val notificationsFlow = MutableStateFlow(
        BleGattCharacteristicValue(
            serviceUuid = UUID(0, 0),
            characteristicUuid = UUID(0, 0),
            value = ByteArray(0)
        )
    )


    override fun connect(address: String) {
        connection.value = BleGattConnectionState.Connected

        val services = List((2..5).random()) {
            BleGattService(
                uuid = UUID.randomUUID(),
                characteristics = List((1..3).random()) {
                    BleGattCharacteristic(
                        UUID.randomUUID(),
                        properties = possibleGattProperties.random()
                    )
                }
            )
        }

        servicesFlow.value = services
    }

    override fun disconnect(address: String) {
        connection.value = BleGattConnectionState.Disconnected
    }

    override fun connectionState(address: String): Flow<BleGattConnectionState> = connection

    override fun services(address: String): Flow<List<BleGattService>> = servicesFlow

    override fun notifications(address: String): Flow<BleGattCharacteristicValue> = notificationsFlow

    override fun read(address: String, service: UUID, characteristic: UUID): Flow<BleGattCharacteristicValue> {
        notificationsFlow.value = BleGattCharacteristicValue(
            serviceUuid = service,
            characteristicUuid = characteristic,
            value = "READ_OK".encodeToByteArray()
        )
        return notificationsFlow
    }

    override fun write(address: String, service: UUID, characteristic: UUID, data: ByteArray) {
        notificationsFlow.value = BleGattCharacteristicValue(
            serviceUuid = service,
            characteristicUuid = characteristic,
            value = data
        )
    }

    override fun enableNotifications(address: String, service: UUID, characteristic: UUID) {
        notificationsFlow.value = BleGattCharacteristicValue(
            serviceUuid = service,
            characteristicUuid = characteristic,
            value = "NOTIFY_ENABLED".encodeToByteArray()
        )
    }
}
