package com.terrsus.terrorwear.features.wifi.udpclient

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Emulator‑safe UDP client that performs no real network operations.
 *
 * Provides a controllable packet stream for UI testing and avoids
 * socket creation on platforms where UDP is not permitted.
 */
class WifiUdpClientFake(
    private val listenPort: Int
) : WifiUdpClient {

    private val incoming = Channel<ByteArray>(Channel.Factory.BUFFERED)

    override val packets: Flow<ByteArray> = incoming.receiveAsFlow()

    override val listeningPort: Int
        get() = listenPort

    override fun start() {
        // No operation
    }

    override fun stop() {
        // No operation
    }

    /**
     * Simulates sending a datagram by echoing the payload back into
     * the incoming packet stream.
     */
    override fun send(data: ByteArray, host: String, port: Int) {
        incoming.trySend(data)
    }
}