package com.terrsus.terrorwear.features.wifi.udpclient

import android.util.Log
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

private const val LogTag = "TW/Wifi/UdpClient"

/**
 * Simple UDP listener/sender for Wi‑Fi traffic.
 *
 * Listens on a fixed local port and exposes incoming datagrams as a cold [kotlinx.coroutines.flow.Flow]
 * of raw [ByteArray] packets. Sending is connectionless and requires host/port
 * per call.
 */
class WifiUdpClientImpl(
    private val listenPort: Int
): WifiUdpClient {
    private val socket = DatagramSocket(listenPort)
    private val incoming = Channel<ByteArray>(Channel.Factory.BUFFERED)

    /** Stream of incoming UDP packets as raw bytes. */
    override val packets: Flow<ByteArray> = incoming.receiveAsFlow()

    /** Local UDP port this client is listening on. */
    override val listeningPort: Int
        get() = listenPort

    /**
     * Sends a UDP datagram to the given [host] and [port] with the provided [data].
     */
    override fun send(data: ByteArray, host: String, port: Int) {
        Log.d(LogTag, "sending $host:$port")

        try {
            val address = InetAddress.getByName(host)
            val packet = DatagramPacket(data, data.size, address, port)
            socket.send(packet)

            Log.d(LogTag, "sent $host:$port")
        } catch (e: Exception) {
            Log.d(LogTag, "sending error $host:$port e=$e")
        }
    }

    /**
     * Starts a background thread that continuously receives UDP packets
     * and forwards them into [packets].
     */
    override fun start() {
        Log.d(LogTag, "starting")

        Thread {
            val buffer = ByteArray(2048)

            try {
                while (!socket.isClosed) {
                    val packet = DatagramPacket(buffer, buffer.size)
                    socket.receive(packet)

                    val receivedBytes = packet.data.copyOf(packet.length)
                    incoming.trySend(receivedBytes)
                }
            }
            catch (e: Exception) {
                Log.d(LogTag, "receive error e=$e")
            }
        }.start()

        Log.d(LogTag, "started")
    }

    /**
     * Stops the UDP listener and closes the underlying socket.
     */
    override fun stop() {
        Log.d(LogTag, "stopping")

        try {
            socket.close()
        } catch (e: Exception) {
            Log.d(LogTag, "stopping error e=$e")
        }

        Log.d(LogTag, "stopped")
    }
}