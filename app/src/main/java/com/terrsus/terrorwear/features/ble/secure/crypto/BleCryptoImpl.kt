package com.terrsus.terrorwear.features.ble.secure.crypto

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.KeyAgreement
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec

/**
 * Concrete crypto implementation for secure BLE.
 *
 * Provides:
 *  - ECDH keypair generation
 *  - shared secret derivation
 *  - HKDF session key derivation
 *  - AES-GCM encryption/decryption
 */
class BleCryptoImpl : BleCrypto {
    // Keypair generation (ECDH)
    override fun generateKeyPair(): KeyPair {
        val kpg = KeyPairGenerator.getInstance("EC")
        kpg.initialize(256)
        return kpg.generateKeyPair()
    }

    // Shared secret derivation (ECDH)
    override fun deriveSharedSecret(privateKey: ByteArray, peerPublicKey: ByteArray): ByteArray {
        val keyFactory = KeyFactory.getInstance("EC")

        val pubKeySpec = X509EncodedKeySpec(peerPublicKey)
        val pubKey = keyFactory.generatePublic(pubKeySpec)

        val privKeySpec = cryptoPrivateKeyFromBytes(privateKey)
        val privKey = keyFactory.generatePrivate(privKeySpec)

        val ka = KeyAgreement.getInstance("ECDH")
        ka.init(privKey)
        ka.doPhase(pubKey, true)

        return ka.generateSecret()
    }

    // Session key derivation (HKDF-SHA256)
    override fun deriveSessionKey(sharedSecret: ByteArray): ByteArray {
        val hkdf = Mac.getInstance("HmacSHA256")
        val salt = ByteArray(32) { 0 } // optional salt
        hkdf.init(SecretKeySpec(salt, "HmacSHA256"))

        return hkdf.doFinal(sharedSecret).copyOf(32) // 256-bit session key
    }

    // AES-GCM encryption
    override fun encrypt(sessionKey: ByteArray, plaintext: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")

        val nonce = generateNonce()
        val key = SecretKeySpec(sessionKey, "AES")

        cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(128, nonce))

        val ciphertext = cipher.doFinal(plaintext)

        // Prepend nonce to ciphertext
        return nonce + ciphertext
    }

    // AES-GCM decryption
    override fun decrypt(sessionKey: ByteArray, encrypted: ByteArray): ByteArray {
        val nonce = encrypted.copyOfRange(0, 12)
        val ciphertext = encrypted.copyOfRange(12, encrypted.size)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val key = SecretKeySpec(sessionKey, "AES")

        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, nonce))

        return cipher.doFinal(ciphertext)
    }

    // Handshake completion signal
    override fun handshakeCompleteSignal(): ByteArray {
        return byteArrayOf(0x7F, 0x7F, 0x7F) // arbitrary "OK" marker
    }

    // Helpers
    private fun generateNonce(): ByteArray {
        return ByteArray(12) { (0..255).random().toByte() } // 96-bit nonce
    }

    private fun cryptoPrivateKeyFromBytes(bytes: ByteArray) =
        PKCS8EncodedKeySpec(bytes)
}
