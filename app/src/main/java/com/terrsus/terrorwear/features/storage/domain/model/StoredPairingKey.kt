package com.terrsus.terrorwear.features.storage.domain.model

/**
 * Represents long‑term pairing material associated with a BLE device.
 *
 * Pairing keys are generated during the secure‑pairing handshake and
 * persisted so that future sessions can be authenticated without repeating
 * the full key‑exchange protocol.
 *
 * @property address MAC address of the device this key belongs to.
 * @property keyBytes Raw key material (AES, ECDH, or protocol‑specific).
 * @property createdAtEpochMillis Timestamp of key creation.
 */
data class StoredPairingKey(
    val address: String,
    val keyBytes: ByteArray,
    val createdAtEpochMillis: Long = System.currentTimeMillis()
)
