package com.terrsus.terrorwear.features.ble.secure.session

/**
 * Represents an active secure BLE session.
 *
 * Provides:
 *  - sessionKey: symmetric key for encryption/decryption
 *  - txCounter / rxCounter: used to derive unique nonces
 *  - nonce helpers for AES-GCM or similar algorithms
 */
interface BleSession {
    val sessionKey: ByteArray

    var txCounter: Long
    var rxCounter: Long

    fun nextTxNonce(): ByteArray
    fun nextRxNonce(): ByteArray
}