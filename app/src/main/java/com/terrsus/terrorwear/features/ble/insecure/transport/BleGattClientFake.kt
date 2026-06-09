package com.terrsus.terrorwear.features.ble.insecure.transport

import com.terrsus.terrorwear.features.ble.common.model.BleGattCharacteristic
import com.terrsus.terrorwear.features.ble.common.model.BleGattCharacteristicValue
import com.terrsus.terrorwear.features.ble.common.model.BleGattConnectionState
import com.terrsus.terrorwear.features.ble.common.model.BleGattService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.UUID
import kotlin.random.Random

/**
 * Fake in-memory implementation of [BleGattClient] used on emulators.
 *
 * Simulates:
 * - connection state transitions
 * - service discovery
 * - characteristic reads/writes
 * - notifications
 *
 * No Android Bluetooth APIs are used.
 */
class BleGattClientFake : BleGattClient {

    private val scope = CoroutineScope(Dispatchers.Default)

    private val connectionStateFlows =
        mutableMapOf<String, MutableStateFlow<BleGattConnectionState>>()

    private val servicesFlows =
        mutableMapOf<String, MutableStateFlow<List<BleGattService>>>()

    private val notificationsFlows =
        mutableMapOf<String, MutableStateFlow<BleGattCharacteristicValue>>()

    // -------------------------------------------------------------------------
    // Fake GATT database
    // -------------------------------------------------------------------------

    private val fakeServiceUuid = UUID.fromString("0000180A-0000-1000-8000-00805f9b34fb")
    private val fakeCharUuid = UUID.fromString("00002A57-0000-1000-8000-00805f9b34fb")

    private val fakeServices = listOf(
        BleGattService(
            uuid = fakeServiceUuid,
            characteristics = listOf(
                BleGattCharacteristic(
                    uuid = fakeCharUuid,
                    properties = 0x0A // read + notify
                )
            )
        )
    )

    // -------------------------------------------------------------------------
    // Connection
    // -------------------------------------------------------------------------

    override fun connect(address: String) {
        val stateFlow = connectionStateFlows.getOrPut(address) {
            MutableStateFlow(BleGattConnectionState.Disconnected)
        }

        stateFlow.value = BleGattConnectionState.Connecting

        scope.launch {
            delay(300)
            stateFlow.value = BleGattConnectionState.Connected

            // Simulate service discovery
            servicesFlows.getOrPut(address) {
                MutableStateFlow(emptyList())
            }.value = fakeServices
        }
    }

    override fun disconnect(address: String) {
        connectionStateFlows[address]?.value = BleGattConnectionState.Disconnected
    }

    override fun connectionState(address: String): Flow<BleGattConnectionState> =
        connectionStateFlows.getOrPut(address) {
            MutableStateFlow(BleGattConnectionState.Disconnected)
        }

    // -------------------------------------------------------------------------
    // Services
    // -------------------------------------------------------------------------

    override fun services(address: String): Flow<List<BleGattService>> =
        servicesFlows.getOrPut(address) {
            MutableStateFlow(emptyList())
        }

    // -------------------------------------------------------------------------
    // Notifications
    // -------------------------------------------------------------------------

    override fun notifications(address: String): Flow<BleGattCharacteristicValue> =
        notificationsFlows.getOrPut(address) {
            MutableStateFlow(
                BleGattCharacteristicValue(
                    serviceUuid = fakeServiceUuid,
                    characteristicUuid = fakeCharUuid,
                    value = ByteArray(0)
                )
            )
        }

    override fun enableNotifications(address: String, service: UUID, characteristic: UUID) {
        // Simulate periodic notifications
        val flow = notificationsFlows.getOrPut(address) {
            MutableStateFlow(
                BleGattCharacteristicValue(
                    serviceUuid = service,
                    characteristicUuid = characteristic,
                    value = ByteArray(0)
                )
            )
        }

        scope.launch {
            while (connectionState(address) == BleGattConnectionState.Connected) {
                delay(1000)
                val randomValue = Random.nextBytes(4)
                flow.update {
                    BleGattCharacteristicValue(
                        serviceUuid = service,
                        characteristicUuid = characteristic,
                        value = randomValue
                    )
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Read / Write
    // -------------------------------------------------------------------------

    override fun read(
        address: String,
        service: UUID,
        characteristic: UUID
    ): Flow<BleGattCharacteristicValue> {

        val flow = notificationsFlows.getOrPut(address) {
            MutableStateFlow(
                BleGattCharacteristicValue(
                    serviceUuid = service,
                    characteristicUuid = characteristic,
                    value = ByteArray(0)
                )
            )
        }

        scope.launch {
            delay(150)
            val randomValue = Random.nextBytes(4)
            flow.update {
                BleGattCharacteristicValue(
                    serviceUuid = service,
                    characteristicUuid = characteristic,
                    value = randomValue
                )
            }
        }

        return flow
    }

    override fun write(
        address: String,
        service: UUID,
        characteristic: UUID,
        data: ByteArray
    ) {
        // Writes simply echo back as a notification
        val flow = notificationsFlows.getOrPut(address) {
            MutableStateFlow(
                BleGattCharacteristicValue(
                    serviceUuid = service,
                    characteristicUuid = characteristic,
                    value = ByteArray(0)
                )
            )
        }

        scope.launch {
            delay(100)
            flow.update {
                BleGattCharacteristicValue(
                    serviceUuid = service,
                    characteristicUuid = characteristic,
                    value = data
                )
            }
        }
    }
}
