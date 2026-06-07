package com.terrsus.terrorwear.features.ble.secure.handshake

import com.terrsus.terrorwear.features.ble.insecure.transport.BleGattClient
import com.terrsus.terrorwear.features.ble.secure.crypto.BleCrypto
import com.terrsus.terrorwear.features.ble.secure.session.BleSession

/**
 * Defines the contract for performing a secure BLE handshake.
 *
 * A handshake is responsible for:
 *  - exchanging public keys
 *  - verifying trust (optional)
 *  - deriving a shared secret
 *  - deriving a session key
 *  - returning a fully initialized [BleSession]
 */
interface HandshakeProtocol {

    /**
     * Perform a full secure handshake with a BLE device.
     *
     * @param address The MAC address of the BLE device.
     * @param insecure The underlying insecure GATT client used for raw reads/writes.
     * @param crypto The cryptographic engine used for key exchange and session key derivation.
     *
     * @return A newly created [BleSession] containing the derived session key and counters.
     */
    suspend fun performHandshake(
        address: String,
        insecure: BleGattClient,
        crypto: BleCrypto
    ): BleSession
}
