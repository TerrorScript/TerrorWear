package com.terrsus.terrorwear.features.wifi.data

import com.terrsus.terrorwear.domain.wifi.model.WifiPacket
import kotlinx.coroutines.flow.Flow

/**
 * High‑level reactive Wi‑Fi API exposed to ViewModels.
 *
 * WifiRepository wraps the low‑level WifiManager and exposes:
 * - reactive packet streams (UDP + TCP)
 * - send operations
 * - lifecycle control for UDP/TCP client/server
 *
 * This interface intentionally hides all socket implementation details.
 */
interface WifiRepository {

    /** Stream of incoming UDP packets. */
    fun udpPackets(): Flow<WifiPacket>

    /** Stream of incoming TCP packets. */
    fun tcpPackets(): Flow<WifiPacket>

    /** Stream of incoming TCP server packets. */
    fun tcpServerPackets(): Flow<WifiPacket>

    /** Sends a UDP packet to the specified host and port. */
    fun sendUdp(data: ByteArray, host: String, port: Int)

    /** Sends a TCP packet to the currently connected TCP server. */
    fun sendTcp(data: ByteArray)

    /** Starts a UDP listener on the given port. */
    fun startUdp(port: Int)

    /** Starts a TCP client and connects to the given host and port. */
    fun startTcpClient(host: String, port: Int)

    /** Starts a TCP server on the given port. */
    fun startTcpServer(port: Int)

    /** Stops all Wi‑Fi communication components. */
    fun stopAll()
}