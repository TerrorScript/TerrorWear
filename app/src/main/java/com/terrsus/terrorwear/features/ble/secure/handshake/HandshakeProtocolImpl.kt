package com.terrsus.terrorwear.features.ble.secure.handshake

import com.terrsus.terrorwear.features.ble.insecure.transport.BleGattClient
import com.terrsus.terrorwear.features.ble.secure.crypto.BleCrypto
import com.terrsus.terrorwear.features.ble.secure.session.BleSession
import com.terrsus.terrorwear.features.ble.secure.session.BleSessionImpl
import kotlinx.coroutines.flow.first
import java.util.UUID

/**
 * Concrete handshake implementation.
 *
 * Responsibilities:
 *  - exchange public keys
 *  - derive shared secret
 *  - derive session key
 *  - return a BleSession
 *
 * Crypto details are delegated to BleCrypto.
 */
class HandshakeProtocolImpl(
    private val crypto: BleCrypto
) : HandshakeProtocol {

    // Replace these UUIDs with your actual handshake service + characteristics
    private val HANDSHAKE_SERVICE = UUID.fromString("0000aaaa-0000-1000-8000-00805f9b34fb")
    private val DEVICE_PUBLIC_KEY = UUID.fromString("0000aa01-0000-1000-8000-00805f9b34fb")
    private val WATCH_PUBLIC_KEY = UUID.fromString("0000aa02-0000-1000-8000-00805f9b34fb")
    private val SESSION_KEY_EXCHANGE = UUID.fromString("0000aa03-0000-1000-8000-00805f9b34fb")

    override suspend fun performHandshake(
        address: String,
        insecure: BleGattClient,
        crypto: BleCrypto
    ): BleSession {

        // Step 1 — Read device public key
        val devicePubKey = insecure
            .read(address, HANDSHAKE_SERVICE, DEVICE_PUBLIC_KEY)
            .first()
            .value

        // Step 2 — Generate our own keypair
        val watchKeyPair = crypto.generateKeyPair()
        val watchPublicKeyBytes = watchKeyPair.public.encoded
        val watchPrivateKeyBytes = watchKeyPair.private.encoded

        // Step 3 — Send our public key to the device
        insecure.write(
            address,
            HANDSHAKE_SERVICE,
            WATCH_PUBLIC_KEY,
            watchPublicKeyBytes
        )

        // Step 4 — Derive shared secret
        val sharedSecret = crypto.deriveSharedSecret(
            privateKey = watchPrivateKeyBytes,
            peerPublicKey = devicePubKey
        )

        // Step 5 — Derive session key
        val sessionKey = crypto.deriveSessionKey(sharedSecret)

        // Step 6 — Notify device handshake is complete
        insecure.write(
            address,
            HANDSHAKE_SERVICE,
            SESSION_KEY_EXCHANGE,
            crypto.handshakeCompleteSignal()
        )

        // Step 7 — Return session object
        return BleSessionImpl(
            sessionKey = sessionKey,
            txCounter = 0,
            rxCounter = 0
        )
    }
}