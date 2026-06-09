package com.terrsus.terrorwear.features.wifi.tcpclient

import com.terrsus.terrorwear.features.wifi.domain.model.WifiEvent
import kotlinx.coroutines.flow.Flow

/**
 * TCP client abstraction for connecting to a remote endpoint
 * and exchanging raw byte streams.
 */
interface WifiTcpClient {

    /**
     * Stream of received TCP packets as raw byte arrays.
     */
    val packets: Flow<ByteArray>

    /**
     * Remote TCP port this client is connected to.
     */
    val connectedPort: Int

    /**
     * Connects to the specified host and port.
     *
     * @param host Remote hostname or IP address.
     * @param port Remote TCP port.
     */
    fun connect(host: String, port: Int)

    /**
     * Sends raw data to the connected endpoint.
     *
     * @param data Raw payload to send.
     */
    fun send(data: ByteArray)

    /**
     * Disconnects from the remote endpoint and releases resources.
     */
    fun disconnect()
}
