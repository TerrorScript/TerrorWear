package com.terrsus.terrorwear.features.ble.secure.session

/**
 * Represents errors that can occur during secure BLE operations.
 */
sealed class SecureBleError {

    /** Handshake failed (crypto, timeout, invalid keys, etc). */
    object HandshakeFailed : SecureBleError()

    /** Encryption or decryption failed. */
    object CryptoError : SecureBleError()

    /** Device disconnected during handshake or session. */
    object Disconnected : SecureBleError()

    /** Device is not trusted or trust verification failed. */
    object TrustVerificationFailed : SecureBleError()

    /** Generic unknown error. */
    data class Unknown(val message: String?) : SecureBleError()
}
