package com.terrsus.terrorwear.data.wifi

import com.terrsus.terrorwear.domain.wifi.model.WifiPacket
import kotlinx.coroutines.flow.Flow

interface WifiRepository {
    fun udpPackets(): Flow<WifiPacket>
    fun tcpPackets(): Flow<WifiPacket>

    fun sendUdp(data: ByteArray, host: String, port: Int)
    fun sendTcp(data: ByteArray)

    fun startUdp(port: Int)
    fun startTcpClient(host: String, port: Int)
    fun startTcpServer(port: Int)
    fun stopAll()
}
