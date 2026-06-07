package com.terrsus.terrorwear.features.storage.encryption

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * AES‑GCM encryption engine used for secure file storage.
 *
 * Handles IV generation, authenticated encryption, and decryption using
 * a Keystore‑backed AES key. The IV is prepended to the ciphertext.
 */
class CryptoEngine(
    private val keyStoreManager: KeyStoreManager
) {

    private val keyAlias = "storage_master_key"

    private fun getKey(): SecretKey =
        keyStoreManager.getOrCreateKey(keyAlias)

    /**
     * Encrypts the given plaintext using AES‑GCM.
     *
     * @return IV + ciphertext.
     */
    fun encrypt(plaintext: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val key = getKey()

        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val ciphertext = cipher.doFinal(plaintext)

        return iv + ciphertext
    }

    /**
     * Decrypts AES‑GCM data produced by [encrypt].
     *
     * @param encrypted Byte array containing IV + ciphertext.
     */
    fun decrypt(encrypted: ByteArray): ByteArray {
        val iv = encrypted.copyOfRange(0, 12)
        val ciphertext = encrypted.copyOfRange(12, encrypted.size)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val key = getKey()

        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))
        return cipher.doFinal(ciphertext)
    }
}