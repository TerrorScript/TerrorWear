package com.terrsus.terrorwear.features.wifi.data

import com.terrsus.terrorwear.domain.wifi.model.WifiPacket
import com.terrsus.terrorwear.features.wifi.WifiManager
import com.terrsus.terrorwear.features.wifi.WifiTcpClient
import com.terrsus.terrorwear.features.wifi.WifiTcpServer
import com.terrsus.terrorwear.features.wifi.WifiUdpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

/**
 * Concrete implementation of [WifiRepository].
 *
 * Wraps [WifiManager] and exposes reactive packet streams.
 * Converts raw byte arrays into domain-level [WifiPacket] objects.
 */
class WifiRepositoryImpl(
    private val manager: WifiManager
) : WifiRepository {

    private var udpClient: WifiUdpClient? = null
    private var tcpClient: WifiTcpClient? = null
    private var tcpServer: WifiTcpServer? = null

    // ---------------------------------------------------------
    // Packet Streams
    // ---------------------------------------------------------

    override fun udpPackets(): Flow<WifiPacket> =
        udpClient?.packets?.map { rawBytes ->
            WifiPacket(
                data = rawBytes,
                from = "udp",
                port = udpClient?.listeningPort ?: 0
            )
        } ?: emptyFlow()

    override fun tcpPackets(): Flow<WifiPacket> =
        tcpClient?.packets?.map { rawBytes ->
            WifiPacket(
                data = rawBytes,
                from = "tcp",
                port = tcpClient?.connectedPort ?: 0
            )
        } ?: emptyFlow()

    override fun tcpServerPackets(): Flow<WifiPacket> =
        tcpServer?.packets?.map { rawBytes ->
            WifiPacket(
                data = rawBytes,
                from = "tcp-server",
                port = tcpServer?.listeningPort ?: 0,
                timestamp = System.currentTimeMillis()
            )
        } ?: emptyFlow()

    // ---------------------------------------------------------
    // Send Operations
    // ---------------------------------------------------------

    override fun sendUdp(data: ByteArray, host: String, port: Int) {
        udpClient?.send(data, host, port)
    }

    override fun sendTcp(data: ByteArray) {
        tcpClient?.send(data)
    }

    // ---------------------------------------------------------
    // Lifecycle Control
    // ---------------------------------------------------------

    override fun startUdp(port: Int) {
        udpClient = manager.startUdp(port)
    }

    override fun startTcpClient(host: String, port: Int) {
        tcpClient = manager.startTcpClient(host, port)
    }

    override fun startTcpServer(port: Int) {
        tcpServer = manager.startTcpServer(port)
    }

    override fun stopAll() {
        manager.stopAll()
        udpClient = null
        tcpClient = null
        tcpServer = null
    }
}