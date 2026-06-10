package com.terrsus.terrorwear.features.wifi.tcpserver

import android.util.Log
import com.terrsus.terrorwear.domain.wifi.model.WifiPacket
import com.terrsus.terrorwear.features.wifi.domain.model.WifiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.net.ServerSocket
import java.net.Socket

private const val LogTag = "TW/Wifi/TcpServer"

/**
 * Simple single‑client TCP server for Wi‑Fi.
 *
 * Listens on a fixed local port, accepts a single client connection, and
 * exposes incoming data as a [kotlinx.coroutines.flow.Flow] of raw [ByteArray] packets.
 */
class WifiTcpServerImpl(
    private val port: Int,
    private val handleWifiEvent: (WifiEvent) -> Unit
) : WifiTcpServer {
    private val serverSocket = ServerSocket(port)
    private var clientSocket: Socket? = null

    /** Local TCP port this server is listening on. */
    override val listeningPort: Int
        get() = port

    private val incoming = Channel<ByteArray>(Channel.Factory.BUFFERED)

    /** Stream of incoming TCP packets from the connected client. */
    override val packets: Flow<ByteArray> = incoming.receiveAsFlow()

    /**
     * Starts a background thread that accepts a single client and then
     * continuously reads from it, emitting packets into [packets].
     */
    override fun start() {
        Thread {
            Log.d(LogTag, "starting port=$port")

            try {
                clientSocket = serverSocket.accept()
                val client = clientSocket!!
                handleWifiEvent(
                    WifiEvent.Connected(
                        host = client.inetAddress.hostAddress!!,
                        port = clientSocket!!.port
                    )
                )
                Log.d(LogTag, "starting client connected")
            } catch (e: Exception) {
                Log.d(LogTag, "starting accept error e=$e")
            }

            try {
                val inputStream = clientSocket!!.getInputStream()
                val buffer = ByteArray(2048)

                while (clientSocket!!.isConnected) {
                    val bytesRead = inputStream.read(buffer)
                    if (bytesRead > 0) {
                        val packetBytes = buffer.copyOf(bytesRead)
                        incoming.trySend(packetBytes)

                        val client = clientSocket!!
                        val host = client.inetAddress.hostAddress
                        val remotePort = client.port
                        handleWifiEvent(
                            WifiEvent.Packet(
                                packet = WifiPacket(
                                    data = packetBytes,
                                    from = host,
                                    port = remotePort
                                )
                            )
                        )

                    } else if (bytesRead < 0) {
                        handleWifiEvent(WifiEvent.Closed(reason = "client disconnected"))
                        break // client disconnected
                    }
                }
            } catch (e: Exception) {
                handleWifiEvent(WifiEvent.Error(
                    message = "read error",
                    cause = e
                ))

                Log.d(LogTag, "read error e=$e")
            }
        }.start()
    }

    /**
     * Sends the given [data] to the connected TCP client, if any.
     */
    override fun send(data: ByteArray) {
        try {
            clientSocket?.getOutputStream()?.write(data)
            clientSocket?.getOutputStream()?.flush()
        } catch (e: Exception) {
            Log.d(LogTag, "send error e=$e")
        }
    }

    /**
     * Stops the server and closes the client connection if present.
     */
    override fun stop() {
        Log.d(LogTag, "stopping")

        try {
            clientSocket?.close()
            serverSocket.close()

            handleWifiEvent(WifiEvent.Closed("server stopped"))

            Log.d(LogTag, "stopped")
        } catch (e: Exception) {
            handleWifiEvent(WifiEvent.Error("stop error", e))

            Log.d(LogTag, "stop error e=$e")
        }
    }
}