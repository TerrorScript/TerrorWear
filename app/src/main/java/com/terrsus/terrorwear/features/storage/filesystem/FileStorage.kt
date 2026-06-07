package com.terrsus.terrorwear.features.storage.filesystem

import java.io.File

/**
 * Basic unencrypted file storage.
 *
 * Useful for non‑sensitive data or debugging scenarios where encryption
 * is unnecessary or undesirable.
 */
class FileStorage(
    private val baseDir: File
) {

    /**
     * Writes raw bytes to a file.
     */
    fun write(name: String, bytes: ByteArray) {
        File(baseDir, name).writeBytes(bytes)
    }

    /**
     * Reads raw bytes from a file if it exists.
     */
    fun read(name: String): ByteArray? {
        val file = File(baseDir, name)
        return if (file.exists()) file.readBytes() else null
    }
}