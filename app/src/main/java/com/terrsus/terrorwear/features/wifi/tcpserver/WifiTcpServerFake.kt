package com.terrsus.terrorwear.features.wifi.tcpserver

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Emulator‑safe TCP server that performs no real network operations.
 *
 * Provides a controllable packet stream for UI testing and avoids
 * socket creation on platforms where TCP servers are not permitted.
 */
class WifiTcpServerFake(
    private val port: Int
) : WifiTcpServer {

    private val incoming = Channel<ByteArray>(Channel.Factory.BUFFERED)

    override val packets: Flow<ByteArray> = incoming.receiveAsFlow()

    override val listeningPort: Int
        get() = port

    override fun start() {
        // No operation
    }

    override fun stop() {
        // No operation
    }

    /**
     * Simulates sending data by echoing the payload back into
     * the incoming packet stream.
     */
    override fun send(data: ByteArray) {
        incoming.trySend(data)
    }
}
