package com.terrsus.terrorwear.features.ble.secure.session

/**
 * Concrete session container for secure BLE communication.
 *
 * Stores:
 *  - sessionKey: derived from handshake
 *  - txCounter: incremented for every encrypted write
 *  - rxCounter: incremented for every decrypted read
 *
 * Counters are used to derive unique nonces for AES-GCM or similar schemes.
 */
class BleSessionImpl(
    override val sessionKey: ByteArray,
    override var txCounter: Long,
    override var rxCounter: Long
) : BleSession {

    override fun nextTxNonce(): ByteArray {
        val nonce = txCounter.toByteArray()
        txCounter++
        return nonce
    }

    override fun nextRxNonce(): ByteArray {
        val nonce = rxCounter.toByteArray()
        rxCounter++
        return nonce
    }

    private fun Long.toByteArray(): ByteArray {
        return byteArrayOf(
            (this shr 56).toByte(),
            (this shr 48).toByte(),
            (this shr 40).toByte(),
            (this shr 32).toByte(),
            (this shr 24).toByte(),
            (this shr 16).toByte(),
            (this shr 8).toByte(),
            this.toByte()
        )
    }
}
