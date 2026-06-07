package com.terrsus.terrorwear.features.storage.filesystem

import com.terrsus.terrorwear.features.storage.encryption.CryptoEngine
import java.io.File

/**
 * File‑based storage backed by AES‑GCM encryption.
 *
 * Each file is encrypted independently using a fresh IV. The encrypted
 * payload is written directly to disk and decrypted on read.
 */
class EncryptedFileStorage(
    private val baseDir: File,
    private val crypto: CryptoEngine
) {

    /**
     * Encrypts and writes a file.
     *
     * @param name File name relative to [baseDir].
     */
    fun write(name: String, plaintext: ByteArray) {
        val encrypted = crypto.encrypt(plaintext)
        File(baseDir, name).writeBytes(encrypted)
    }

    /**
     * Reads and decrypts a file if it exists.
     *
     * @return Decrypted bytes or null if the file is missing.
     */
    fun read(name: String): ByteArray? {
        val file = File(baseDir, name)
        if (!file.exists()) return null
        return crypto.decrypt(file.readBytes())
    }
}