package com.terrsus.terrorwear.features.wifi.tcpclient

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

/**
 * Simple TCP client for Wi‑Fi connections.
 *
 * Connects to a single remote host/port and exposes incoming data as a [kotlinx.coroutines.flow.Flow]
 * of raw [ByteArray] packets. Outgoing data is written directly to the socket.
 */
class WifiTcpClientImpl: WifiTcpClient {

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
        remotePort = port

        socket = Socket(host, port)
        inputStream = socket!!.getInputStream()
        outputStream = socket!!.getOutputStream()

        Thread {
            val buffer = ByteArray(2048)

            while (socket?.isConnected == true) {
                val bytesRead = inputStream!!.read(buffer)
                if (bytesRead > 0) {
                    val packetBytes = buffer.copyOf(bytesRead)
                    incoming.trySend(packetBytes)
                } else if (bytesRead < 0) {
                    break // connection closed
                }
            }
        }.start()
    }

    /**
     * Sends the given [data] to the connected TCP endpoint.
     */
    override fun send(data: ByteArray) {
        outputStream?.write(data)
        outputStream?.flush()
    }

    /**
     * Closes the underlying socket. The read loop will eventually terminate.
     */
    override fun disconnect() {
        socket?.close()
    }
}