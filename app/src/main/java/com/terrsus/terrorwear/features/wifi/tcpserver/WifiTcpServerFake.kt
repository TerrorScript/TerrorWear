package com.terrsus.terrorwear.features.wifi.tcpserver

import com.terrsus.terrorwear.domain.wifi.model.WifiPacket
import com.terrsus.terrorwear.features.wifi.domain.model.WifiEvent
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
    private val port: Int,
    private val handleWifiEvent: (WifiEvent) -> Unit
) : WifiTcpServer {

    private val incoming = Channel<ByteArray>(Channel.Factory.BUFFERED)

    override val packets: Flow<ByteArray> = incoming.receiveAsFlow()

    override val listeningPort: Int
        get() = port

    override fun start() {
        handleWifiEvent(WifiEvent.Listening(port))
    }

    override fun stop() {
        handleWifiEvent(WifiEvent.Closed("fake server stopped"))
    }

    /**
     * Simulates sending data by echoing the payload back into
     * the incoming packet stream.
     */
    override fun send(data: ByteArray) {
        incoming.trySend(data)
        handleWifiEvent(WifiEvent.Packet(WifiPacket(data, "local", port)))
    }
}
