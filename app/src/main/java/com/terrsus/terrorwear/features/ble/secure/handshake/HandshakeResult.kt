package com.terrsus.terrorwear.features.ble.secure.handshake

data class HandshakeResult(
    val sessionKey: ByteArray,
    val devicePublicKey: ByteArray,
    val watchPublicKey: ByteArray
)