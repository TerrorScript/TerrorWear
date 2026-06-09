package com.terrsus.terrorwear.features.wifi.udpclient

import kotlinx.coroutines.flow.Flow

/**
 * UDP client abstraction for sending and receiving datagrams.
 *
 * Implementations may provide real socket access or a simulated
 * environment for platforms where UDP is unavailable.
 */
interface WifiUdpClient {

    /**
     * Stream of received UDP packets as raw byte arrays.
     */
    val packets: Flow<ByteArray>

    /**
     * Local port the client listens on.
     */
    val listeningPort: Int

    /**
     * Starts the UDP listener.
     */
    fun start()

    /**
     * Stops the UDP listener and releases resources.
     */
    fun stop()

    /**
     * Sends a UDP datagram to the specified host and port.
     *
     * @param data Raw payload to send.
     * @param host Destination hostname or IP address.
     * @param port Destination UDP port.
     */
    fun send(data: ByteArray, host: String, port: Int)
}