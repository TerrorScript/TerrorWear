package com.terrsus.terrorwear.features.ble.secure.manager

import com.terrsus.terrorwear.features.ble.common.model.BleGattService
import com.terrsus.terrorwear.features.ble.secure.session.SecureBleConnectionState
import kotlinx.coroutines.flow.Flow

/**
 * High-level manager for secure BLE operations.
 *
 * Wraps SecureBleGattClient and exposes a simplified API for modules.
 */
interface SecureBleManager {

    /**
     * Start a secure connection to the device.
     *
     * @param address The MAC address of the BLE device.
     */
    fun connect(address: String)

    /**
     * Disconnect from the device and clear session state.
     *
     * @param address The MAC address of the BLE device.
     */
    fun disconnect(address: String)

    /**
     * Observe secure connection state.
     *
     * @param address The MAC address of the BLE device.
     * @return A flow emitting secure connection state updates.
     */
    fun connectionState(address: String): Flow<SecureBleConnectionState>

    /**
     * Observe discovered services after secure handshake completes.
     *
     * @param address The MAC address of the BLE device.
     * @return A flow emitting the list of discovered services.
     */
    fun services(address: String): Flow<List<BleGattService>>
}
