package com.terrsus.terrorwear.features.wifi.tcpclient

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Emulator‑safe TCP client that performs no real network operations.
 *
 * Provides a controllable packet stream for UI testing and avoids
 * socket creation on platforms where TCP clients are not permitted.
 */
class WifiTcpClientFake : WifiTcpClient {

    private val incoming = Channel<ByteArray>(Channel.Factory.BUFFERED)

    override val packets: Flow<ByteArray> = incoming.receiveAsFlow()

    private var port: Int = 0

    override val connectedPort: Int
        get() = port

    override fun connect(host: String, port: Int) {
        this.port = port
    }

    override fun send(data: ByteArray) {
        incoming.trySend(data)
    }

    override fun disconnect() {
        // No operation
    }
}
