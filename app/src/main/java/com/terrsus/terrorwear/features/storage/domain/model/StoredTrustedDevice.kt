package com.terrsus.terrorwear.features.storage.domain.model

/**
 * Represents a BLE device that has been marked as trusted.
 */
data class StoredTrustedDevice(
    val address: String,
    val name: String? = null,
    val lastSeenEpochMillis: Long = System.currentTimeMillis()
)
