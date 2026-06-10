package com.terrsus.terrorwear.features.wifi.udpclient

import android.util.Log
import com.terrsus.terrorwear.domain.wifi.model.WifiPacket
import com.terrsus.terrorwear.features.wifi.domain.model.WifiEvent
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
    private val listenPort: Int,
    private val handleWifiEvent: (WifiEvent) -> Unit
) : WifiUdpClient {

    private val incoming = Channel<ByteArray>(Channel.Factory.BUFFERED)

    override val packets: Flow<ByteArray> = incoming.receiveAsFlow()

    override val listeningPort: Int
        get() = listenPort

    override fun start() {
        Log.d(LogTag, "starting")

        handleWifiEvent(WifiEvent.Listening(listenPort))

        Log.d(LogTag, "starting")
    }

    override fun stop() {
        Log.d(LogTag, "stopping")

        handleWifiEvent(WifiEvent.Closed("fake udp stopped"))

        Log.d(LogTag, "stopped")
    }

    /**
     * Simulates sending a datagram by echoing the payload back into
     * the incoming packet stream.
     */
    override fun send(data: ByteArray, host: String, port: Int) {
        Log.d(LogTag, "sending $host:$port")

        incoming.trySend(data)
        handleWifiEvent(WifiEvent.Packet(
            packet = WifiPacket(
                data = data,
                from = host,
                port = port
            )
        ))

        Log.d(LogTag, "sent $host:$port")
    }
}