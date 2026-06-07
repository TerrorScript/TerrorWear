package com.terrsus.terrorwear.features.storage.domain.usecase

import com.terrsus.terrorwear.features.storage.domain.model.StoredTrustedDevice
import com.terrsus.terrorwear.features.storage.domain.repository.TrustedDeviceRepository

/**
 * Provides operations for managing trusted BLE devices.
 *
 * Trusted devices represent BLE peers that the user has explicitly approved
 * or paired with. This interactor centralizes loading and saving operations
 * for these records.
 */
class TrustedDeviceInteractor(
    private val repository: TrustedDeviceRepository
) {

    /**
     * Loads all trusted BLE devices currently stored.
     */
    suspend fun loadAll(): List<StoredTrustedDevice> =
        repository.loadAll()

    /**
     * Saves or updates a trusted BLE device entry.
     */
    suspend fun save(device: StoredTrustedDevice) {
        repository.save(device)
    }

    /**
     * Convenience overload for when only the device address is known.
     */
    suspend fun save(address: String) {
        repository.save(
            StoredTrustedDevice(
                address = address,
                name = null,
                lastSeenEpochMillis = System.currentTimeMillis()
            )
        )
    }
}
