package com.terrsus.terrorwear.features.wifi.udpclient

import android.util.Log
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

private const val LogTag = "TW/Wifi/UdpClient"

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
        Log.d(LogTag, "starting")

        // No operation

        Log.d(LogTag, "starting")
    }

    override fun stop() {
        Log.d(LogTag, "stopping")

        // No operation

        Log.d(LogTag, "stopped")
    }

    /**
     * Simulates sending a datagram by echoing the payload back into
     * the incoming packet stream.
     */
    override fun send(data: ByteArray, host: String, port: Int) {
        Log.d(LogTag, "sending $host:$port")

        incoming.trySend(data)

        Log.d(LogTag, "sent $host:$port")
    }
}