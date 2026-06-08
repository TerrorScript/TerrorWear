package com.terrsus.terrorwear.features.wifi

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.net.ServerSocket
import java.net.Socket

/**
 * Simple single‑client TCP server for Wi‑Fi.
 *
 * Listens on a fixed local port, accepts a single client connection, and
 * exposes incoming data as a [Flow] of raw [ByteArray] packets.
 */
class WifiTcpServer(
    private val port: Int
) {
    private val serverSocket = ServerSocket(port)
    private var clientSocket: Socket? = null

    /** Local TCP port this server is listening on. */
    val listeningPort: Int
        get() = port

    private val incoming = Channel<ByteArray>(Channel.BUFFERED)

    /** Stream of incoming TCP packets from the connected client. */
    val packets: Flow<ByteArray> = incoming.receiveAsFlow()

    /**
     * Starts a background thread that accepts a single client and then
     * continuously reads from it, emitting packets into [packets].
     */
    fun start() {
        Thread {
            clientSocket = serverSocket.accept()
            val inputStream = clientSocket!!.getInputStream()

            val buffer = ByteArray(2048)

            while (clientSocket!!.isConnected) {
                val bytesRead = inputStream.read(buffer)
                if (bytesRead > 0) {
                    val packetBytes = buffer.copyOf(bytesRead)
                    incoming.trySend(packetBytes)
                } else if (bytesRead < 0) {
                    break // client disconnected
                }
            }
        }.start()
    }

    /**
     * Sends the given [data] to the connected TCP client, if any.
     */
    fun send(data: ByteArray) {
        clientSocket?.getOutputStream()?.write(data)
        clientSocket?.getOutputStream()?.flush()
    }

    /**
     * Stops the server and closes the client connection if present.
     */
    fun stop() {
        clientSocket?.close()
        serverSocket.close()
    }
}