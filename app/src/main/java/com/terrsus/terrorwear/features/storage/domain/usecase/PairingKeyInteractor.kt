package com.terrsus.terrorwear.features.storage.domain.usecase

import com.terrsus.terrorwear.features.storage.domain.model.StoredPairingKey
import com.terrsus.terrorwear.features.storage.domain.repository.PairingKeyRepository

/**
 * Provides operations for loading and saving long-term BLE pairing keys.
 *
 * Pairing keys are used by the secure BLE pipeline to authenticate devices
 * and resume encrypted sessions. This interactor centralizes all pairing-key
 * related operations.
 */
class PairingKeyInteractor(
    private val repository: PairingKeyRepository
) {

    /**
     * Loads the pairing key associated with the given BLE device address.
     *
     * @param address BLE MAC address of the device.
     * @return The stored pairing key, or null if none exists.
     */
    suspend fun load(address: String): StoredPairingKey? =
        repository.load(address)

    /**
     * Saves or updates the pairing key for a BLE device.
     */
    suspend fun save(key: StoredPairingKey) {
        repository.save(key)
    }

    /**
     * Convenience overload for when only address + raw key bytes are known.
     */
    suspend fun save(address: String, keyBytes: ByteArray) {
        repository.save(
            StoredPairingKey(
                address = address,
                keyBytes = keyBytes,
                createdAtEpochMillis = System.currentTimeMillis()
            )
        )
    }
}
