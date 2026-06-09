package com.terrsus.terrorwear.features.wifi.tcpclient

import android.util.Log
import com.terrsus.terrorwear.features.wifi.domain.model.WifiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

private const val LogTag = "TW/Wifi/TcpClient"

/**
 * Emulator‑safe TCP client that performs no real network operations.
 *
 * Provides a controllable packet stream for UI testing and avoids
 * socket creation on platforms where TCP clients are not permitted.
 */
class WifiTcpClientFake(
    private val wifiEventCallback: (WifiEvent) -> Unit
) : WifiTcpClient {

    private val incoming = Channel<ByteArray>(Channel.Factory.BUFFERED)

    override val packets: Flow<ByteArray> = incoming.receiveAsFlow()

    private var port: Int = 0

    override val connectedPort: Int
        get() = port

    override fun connect(host: String, port: Int) {
        Log.d(LogTag, "connect $host:$port")

        this.port = port
        wifiEventCallback(WifiEvent.Connected(host, port))
    }

    override fun send(data: ByteArray) {
        Log.d(LogTag, "send")

        incoming.trySend(data)
    }

    override fun disconnect() {
        Log.d(LogTag, "disconnecting")

        wifiEventCallback(WifiEvent.Closed("Fake disconnect"))

        Log.d(LogTag, "disconnected")
    }
}
