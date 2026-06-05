package com.terrsus.terrorwear.features.wifi

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class WifiUdpClient(
    private val listenPort: Int
) {
    private val socket = DatagramSocket(listenPort)
    private val incoming = Channel<ByteArray>(Channel.BUFFERED)

    val packets: Flow<ByteArray> = incoming.receiveAsFlow()

    fun send(data: ByteArray, host: String, port: Int) {
        val addr = InetAddress.getByName(host)
        val packet = DatagramPacket(data, data.size, addr, port)
        socket.send(packet)
    }

    fun start() {
        Thread {
            val buf = ByteArray(2048)
            while (!socket.isClosed) {
                val packet = DatagramPacket(buf, buf.size)
                socket.receive(packet)
                incoming.trySend(packet.data.copyOf(packet.length))
            }
        }.start()
    }

    fun stop() {
        socket.close()
    }
}
