package com.terrsus.terrorwear.features.ble.secure.crypto

import java.security.KeyPair

/**
 * Cryptographic operations for secure BLE communication.
 *
 * Provides:
 *  - ECDH keypair generation
 *  - shared secret derivation
 *  - HKDF session key derivation
 *  - AES-GCM encryption/decryption
 *  - handshake completion marker
 */
interface BleCrypto {

    /**
     * Generate an ECDH keypair (typically P-256).
     *
     * @return A newly generated elliptic-curve keypair.
     */
    fun generateKeyPair(): KeyPair

    /**
     * Derive a shared secret using ECDH.
     *
     * @param privateKey The local private key in encoded byte form.
     * @param peerPublicKey The remote device's public key in encoded byte form.
     * @return The raw shared secret produced by ECDH.
     */
    fun deriveSharedSecret(
        privateKey: ByteArray,
        peerPublicKey: ByteArray
    ): ByteArray

    /**
     * Derive a symmetric session key from the shared secret using HKDF-SHA256.
     *
     * @param sharedSecret The raw ECDH shared secret.
     * @return A 256-bit session key suitable for AES-GCM.
     */
    fun deriveSessionKey(sharedSecret: ByteArray): ByteArray

    /**
     * Encrypt plaintext using AES-GCM with the given session key.
     *
     * @param sessionKey The symmetric session key.
     * @param plaintext The unencrypted payload.
     * @return The encrypted payload, typically [nonce | ciphertext].
     */
    fun encrypt(sessionKey: ByteArray, plaintext: ByteArray): ByteArray

    /**
     * Decrypt ciphertext using AES-GCM with the given session key.
     *
     * @param sessionKey The symmetric session key.
     * @param encrypted The encrypted payload, typically [nonce | ciphertext].
     * @return The decrypted plaintext.
     */
    fun decrypt(sessionKey: ByteArray, encrypted: ByteArray): ByteArray

    /**
     * Marker sent to the device to indicate handshake completion.
     *
     * @return A small byte sequence signaling handshake success.
     */
    fun handshakeCompleteSignal(): ByteArray
}