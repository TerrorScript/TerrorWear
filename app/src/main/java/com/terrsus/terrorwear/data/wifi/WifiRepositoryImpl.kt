package com.terrsus.terrorwear.data.wifi

import com.terrsus.terrorwear.domain.wifi.model.WifiPacket
import com.terrsus.terrorwear.features.wifi.WifiManager
import com.terrsus.terrorwear.features.wifi.WifiTcpClient
import com.terrsus.terrorwear.features.wifi.WifiTcpServer
import com.terrsus.terrorwear.features.wifi.WifiUdpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class WifiRepositoryImpl(
    private val manager: WifiManager
) : WifiRepository {

    private var udp: WifiUdpClient? = null
    private var tcp: WifiTcpClient? = null
    private var server: WifiTcpServer? = null

    override fun udpPackets(): Flow<WifiPacket> =
        udp?.packets?.map { WifiPacket(it, "udp", 0) } ?: emptyFlow()

    override fun tcpPackets(): Flow<WifiPacket> =
        tcp?.packets?.map { WifiPacket(it, "tcp", 0) } ?: emptyFlow()

    override fun sendUdp(data: ByteArray, host: String, port: Int) {
        udp?.send(data, host, port)
    }

    override fun sendTcp(data: ByteArray) {
        tcp?.send(data)
    }

    override fun startUdp(port: Int) {
        udp = manager.startUdp(port)
    }

    override fun startTcpClient(host: String, port: Int) {
        tcp = manager.startTcpClient(host, port)
    }

    override fun startTcpServer(port: Int) {
        server = manager.startTcpServer(port)
    }

    override fun stopAll() {
        manager.stopAll()
    }
}
