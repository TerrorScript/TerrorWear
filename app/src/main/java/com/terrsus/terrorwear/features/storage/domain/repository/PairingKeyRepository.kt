package com.terrsus.terrorwear.features.storage.domain.repository

import com.terrsus.terrorwear.features.storage.domain.model.StoredPairingKey

/**
 * Repository abstraction for long‑term BLE pairing keys.
 *
 * Implementations are responsible for secure persistence of key material.
 */
interface PairingKeyRepository {

    /**
     * Loads the pairing key associated with the given device address.
     *
     * @return The stored key, or null if no key exists.
     */
    suspend fun load(address: String): StoredPairingKey?

    /**
     * Inserts or updates the pairing key for a device.
     */
    suspend fun save(pairingKey: StoredPairingKey)
}