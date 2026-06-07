package com.terrsus.terrorwear.features.ble.secure.transport

import com.terrsus.terrorwear.features.ble.common.model.BleGattConnectionState
import com.terrsus.terrorwear.features.ble.common.model.BleGattService
import com.terrsus.terrorwear.features.ble.common.model.BleGattCharacteristicValue
import com.terrsus.terrorwear.features.ble.secure.session.BleSession
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Secure wrapper around the insecure BleGattClient.
 *
 * Handles:
 *  - handshake
 *  - session establishment
 *  - encryption/decryption
 *  - secure notifications
 */
interface SecureBleGattClient {

    fun connectSecure(address: String)

    fun disconnect(address: String)

    fun connectionState(address: String): Flow<BleGattConnectionState>

    fun services(address: String): Flow<List<BleGattService>>

    fun secureNotifications(address: String): Flow<BleGattCharacteristicValue>

    fun secureRead(
        address: String,
        service: UUID,
        characteristic: UUID
    ): Flow<BleGattCharacteristicValue>

    fun secureWrite(
        address: String,
        service: UUID,
        characteristic: UUID,
        plaintext: ByteArray
    )

    fun enableSecureNotifications(
        address: String,
        service: UUID,
        characteristic: UUID
    )

    /** Expose the active secure session (for sessionKey persistence). */
    fun currentSession(address: String): BleSession?
}
