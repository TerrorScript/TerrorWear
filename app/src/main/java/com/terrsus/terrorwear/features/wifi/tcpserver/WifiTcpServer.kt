package com.terrsus.terrorwear.features.wifi.tcpserver

import kotlinx.coroutines.flow.Flow

/**
 * TCP server abstraction for accepting a single client and
 * exchanging raw byte streams.
 */
interface WifiTcpServer {

    /**
     * Stream of received TCP packets as raw byte arrays.
     */
    val packets: Flow<ByteArray>

    /**
     * Local TCP port this server listens on.
     */
    val listeningPort: Int

    /**
     * Starts the TCP server and begins accepting a client.
     */
    fun start()

    /**
     * Stops the server and closes any active client connection.
     */
    fun stop()

    /**
     * Sends raw data to the connected client, if present.
     *
     * @param data Raw payload to send.
     */
    fun send(data: ByteArray)
}
