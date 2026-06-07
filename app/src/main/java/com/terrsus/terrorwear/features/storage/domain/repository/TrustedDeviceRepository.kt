package com.terrsus.terrorwear.features.storage.domain.repository

import com.terrsus.terrorwear.features.storage.domain.model.StoredTrustedDevice

/**
 * Repository abstraction for trusted‑device persistence.
 *
 * Implementations may use Room, encrypted files, or any other backend.
 * The domain layer depends only on this interface.
 */
interface TrustedDeviceRepository {

    /**
     * Loads all trusted BLE devices from storage.
     */
    suspend fun loadAll(): List<StoredTrustedDevice>

    /**
     * Inserts or updates a trusted BLE device entry.
     */
    suspend fun save(device: StoredTrustedDevice)
}