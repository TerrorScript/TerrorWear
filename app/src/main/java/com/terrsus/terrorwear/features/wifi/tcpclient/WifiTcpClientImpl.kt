package com.terrsus.terrorwear.features.wifi.tcpclient

import android.util.Log
import com.terrsus.terrorwear.features.wifi.domain.model.WifiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

private const val LogTag = "TW/Wifi/TcpClient"

/**
 * Simple TCP client for Wi‑Fi connections.
 *
 * Connects to a single remote host/port and exposes incoming data as a [kotlinx.coroutines.flow.Flow]
 * of raw [ByteArray] packets. Outgoing data is written directly to the socket.
 */
class WifiTcpClientImpl(
    private val wifiEventCallback: (WifiEvent) -> Unit
) : WifiTcpClient {

    private var socket: Socket? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

    // Port of the remote endpoint we are connected to.
    private var remotePort: Int = 0

    /** Remote TCP port this client is currently connected to. */
    override val connectedPort: Int
        get() = remotePort

    private val incoming = Channel<ByteArray>(Channel.Factory.BUFFERED)

    /** Stream of incoming TCP packets as raw bytes. */
    override val packets: Flow<ByteArray> = incoming.receiveAsFlow()

    /**
     * Connects to the given [host] and [port], and starts a background thread
     * that continuously reads from the socket and emits packets into [packets].
     */
    override fun connect(host: String, port: Int) {
        Log.d(LogTag, "connecting $host:$port")

        try {
            remotePort = port

            socket = Socket(host, port)
            inputStream = socket!!.getInputStream()
            outputStream = socket!!.getOutputStream()

            wifiEventCallback(WifiEvent.Connected(host, port))

            Log.d(LogTag, "connected $host:$port")
        } catch (e: Exception) {
            wifiEventCallback(WifiEvent.Error(e.message ?: "unknown error", e))

            Log.d(LogTag, "connect error e=$e")
            return
        }

        Thread {
            val buffer = ByteArray(2048)

            try {
                while (socket != null && !socket!!.isClosed) {
                    val bytesRead = inputStream!!.read(buffer)
                    if (bytesRead > 0) {
                        val packetBytes = buffer.copyOf(bytesRead)
                        incoming.trySend(packetBytes)

                    } else if (bytesRead < 0) {
                        // connection closed
                        wifiEventCallback(WifiEvent.Closed("remote closed"))
                        break
                    }
                }
            } catch (e: Exception) {
                wifiEventCallback(WifiEvent.Error("read error", e))

                Log.d(LogTag, "read error e=$e")
            }
        }.start()
    }

    /**
     * Sends the given [data] to the connected TCP endpoint.
     */
    override fun send(data: ByteArray) {
        Log.d(LogTag, "sending remotePort=$remotePort")

        try {
            outputStream?.write(data)
            outputStream?.flush()

            Log.d(LogTag, "sent remotePort=$remotePort")
        } catch (e: Exception) {
            wifiEventCallback(WifiEvent.Error(e.message ?: "unknown error", e))

            Log.d(LogTag, "send error remotePort=$remotePort e=$e")
        }
    }

    /**
     * Closes the underlying socket. The read loop will eventually terminate.
     */
    override fun disconnect() {
        Log.d(LogTag, "disconnecting")

        try {
            inputStream?.close()
            outputStream?.close()
            socket?.close()
            remotePort = 0

            wifiEventCallback(WifiEvent.Closed("local disconnect"))

            Log.d(LogTag, "disconnected")
        } catch (e: Exception) {
            wifiEventCallback(WifiEvent.Error(e.message ?: "unknown error", e))

            Log.d(LogTag, "disconnect error e=$e")
        }
    }
}