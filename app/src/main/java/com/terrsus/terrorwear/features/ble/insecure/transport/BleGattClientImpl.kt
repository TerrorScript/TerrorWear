package com.terrsus.terrorwear.features.ble.insecure.transport

import android.Manifest
import android.bluetooth.*
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import com.terrsus.terrorwear.features.ble.common.model.BleGattConnectionState
import com.terrsus.terrorwear.features.ble.common.model.BleGattService
import com.terrsus.terrorwear.features.ble.common.model.BleGattCharacteristic
import com.terrsus.terrorwear.features.ble.common.model.BleGattCharacteristicValue
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
        mutableMapOf<String, MutableStateFlow<BleGattCharacteristicValue>>()

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun connect(address: String) {
        val device = bluetoothAdapter.getRemoteDevice(address)

        val stateFlow = connectionStateFlows.getOrPut(address) {
            MutableStateFlow(BleGattConnectionState.Disconnected)
        }

        stateFlow.value = BleGattConnectionState.Connecting

        device.connectGatt(context, false, object : BluetoothGattCallback() {

            @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
            override fun onConnectionStateChange(
                gatt: BluetoothGatt,
                status: Int,
                newState: Int
            ) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d("TW/Gatt", "Connected to $address")
                    gattMap[address] = gatt
                    stateFlow.value = BleGattConnectionState.Connected
                    gatt.discoverServices()
                } else {
                    Log.d("TW/Gatt", "Disconnected from $address")
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
                    MutableStateFlow(
                        BleGattCharacteristicValue(
                            serviceUuid = characteristic.service.uuid,
                            characteristicUuid = characteristic.uuid,
                            value = ByteArray(0)
                        )
                    )
                }.value = BleGattCharacteristicValue(
                    serviceUuid = characteristic.service.uuid,
                    characteristicUuid = characteristic.uuid,
                    value = characteristic.value
                )
            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                status: Int
            ) {
                notificationsFlows.getOrPut(address) {
                    MutableStateFlow(
                        BleGattCharacteristicValue(
                            serviceUuid = characteristic.service.uuid,
                            characteristicUuid = characteristic.uuid,
                            value = ByteArray(0)
                        )
                    )
                }.value = BleGattCharacteristicValue(
                    serviceUuid = characteristic.service.uuid,
                    characteristicUuid = characteristic.uuid,
                    value = characteristic.value
                )
            }
        })
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
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

    override fun notifications(address: String): Flow<BleGattCharacteristicValue> =
        notificationsFlows.getOrPut(address) {
            MutableStateFlow(
                BleGattCharacteristicValue(
                    serviceUuid = UUID(0, 0),
                    characteristicUuid = UUID(0, 0),
                    value = ByteArray(0)
                )
            )
        }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun read(address: String, service: UUID, characteristic: UUID): Flow<BleGattCharacteristicValue> {
        val gatt = gattMap[address] ?: return notifications(address)

        val ch = gatt.getService(service)?.getCharacteristic(characteristic)
            ?: return notifications(address)

        if (Build.VERSION.SDK_INT >= 33) {
            gatt.readCharacteristic(ch)
        } else {
            gatt.readCharacteristic(ch)
        }

        return notifications(address)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun write(address: String, service: UUID, characteristic: UUID, data: ByteArray) {
        val gatt = gattMap[address] ?: return
        val ch = gatt.getService(service)?.getCharacteristic(characteristic) ?: return

        if (Build.VERSION.SDK_INT >= 33) {
            gatt.writeCharacteristic(ch, data, BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
        } else {
            ch.value = data
            gatt.writeCharacteristic(ch)
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun enableNotifications(address: String, service: UUID, characteristic: UUID) {
        val gatt = gattMap[address] ?: return

        val ch = gatt.getService(service)?.getCharacteristic(characteristic) ?: return

        gatt.setCharacteristicNotification(ch, true)

        val descriptor = ch.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG))
        descriptor ?: return

        if (Build.VERSION.SDK_INT >= 33) {
            gatt.writeDescriptor(descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
        } else {
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt.writeDescriptor(descriptor)
        }
    }

    companion object {
        private const val CLIENT_CHARACTERISTIC_CONFIG =
            "00002902-0000-1000-8000-00805f9b34fb"
    }
}
