package com.terrsus.terrorwear.domain.wifi.model

data class WifiPacket(
    val data: ByteArray,
    val from: String,
    val port: Int
)
