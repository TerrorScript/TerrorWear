package com.terrsus.terrorwear.features.ble.gatt

import android.bluetooth.*
import android.content.Context
import android.util.Log
import com.terrsus.terrorwear.features.ble.gatt.model.BleGattService
import com.terrsus.terrorwear.features.ble.gatt.model.BleGattCharacteristic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class BleGattClientImpl(
    private val context: Context
) : BleGattClient {

    private val bluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    private val bluetoothAdapter = bluetoothManager.adapter

    private val gattMap = mutableMapOf<String, BluetoothGatt>()

    private val connectionStateFlows =
        mutableMapOf<String, MutableStateFlow<BleGattConnectionState>>()

    private val servicesFlows =
        mutableMapOf<String, MutableStateFlow<List<BleGattService>>>()

    private val notificationsFlows =
        mutableMapOf<String, MutableStateFlow<ByteArray>>()

    override fun connect(address: String) {
        val device = bluetoothAdapter.getRemoteDevice(address)

        val stateFlow = connectionStateFlows.getOrPut(address) {
            MutableStateFlow(BleGattConnectionState.Disconnected)
        }

        stateFlow.value = BleGattConnectionState.Connecting

        device.connectGatt(context, false, object : BluetoothGattCallback() {

            override fun onConnectionStateChange(
                gatt: BluetoothGatt,
                status: Int,
                newState: Int
            ) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d("Gatt", "Connected to $address")
                    gattMap[address] = gatt
                    stateFlow.value = BleGattConnectionState.Connected
                    gatt.discoverServices()
                } else {
                    Log.d("Gatt", "Disconnected from $address")
                    stateFlow.value = BleGattConnectionState.Disconnected
                    gattMap.remove(address)
                }
            }

            override fun onServicesDiscovered(
                gatt: BluetoothGatt,
                status: Int
            ) {
                if (status != BluetoothGatt.GATT_SUCCESS) return

                val services = gatt.services.map { service ->
                    BleGattService(
                        uuid = service.uuid,
                        characteristics = service.characteristics.map { ch ->
                            BleGattCharacteristic(
                                uuid = ch.uuid,
                                properties = ch.properties
                            )
                        }
                    )
                }

                servicesFlows.getOrPut(address) {
                    MutableStateFlow(emptyList())
                }.value = services
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic
            ) {
                notificationsFlows.getOrPut(address) {
                    MutableStateFlow(ByteArray(0))
                }.value = characteristic.value
            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                status: Int
            ) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    notificationsFlows.getOrPut(address) {
                        MutableStateFlow(ByteArray(0))
                    }.value = characteristic.value
                }
            }
        })
    }

    override fun disconnect(address: String) {
        gattMap[address]?.close()
        gattMap.remove(address)
        connectionStateFlows[address]?.value = BleGattConnectionState.Disconnected
    }

    override fun connectionState(address: String): Flow<BleGattConnectionState> =
        connectionStateFlows.getOrPut(address) {
            MutableStateFlow(BleGattConnectionState.Disconnected)
        }

    override fun services(address: String): Flow<List<BleGattService>> =
        servicesFlows.getOrPut(address) {
            MutableStateFlow(emptyList())
        }

    override fun notifications(address: String): Flow<ByteArray> =
        notificationsFlows.getOrPut(address) {
            MutableStateFlow(ByteArray(0))
        }

    override fun read(address: String, service: UUID, characteristic: UUID): Flow<ByteArray> {
        val gatt = gattMap[address] ?: return notifications(address)

        val ch = gatt.getService(service)?.getCharacteristic(characteristic)
            ?: return notifications(address)

        gatt.readCharacteristic(ch)

        return notifications(address)
    }

    override fun write(address: String, service: UUID, characteristic: UUID, data: ByteArray) {
        val gatt = gattMap[address] ?: return

        val ch = gatt.getService(service)?.getCharacteristic(characteristic) ?: return

        ch.value = data
        gatt.writeCharacteristic(ch)
    }

    override fun enableNotifications(address: String, service: UUID, characteristic: UUID) {
        val gatt = gattMap[address] ?: return

        val ch = gatt.getService(service)?.getCharacteristic(characteristic) ?: return

        gatt.setCharacteristicNotification(ch, true)

        val descriptor = ch.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG))
        descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        gatt.writeDescriptor(descriptor)
    }

    companion object {
        private const val CLIENT_CHARACTERISTIC_CONFIG =
            "00002902-0000-1000-8000-00805f9b34fb"
    }
}
