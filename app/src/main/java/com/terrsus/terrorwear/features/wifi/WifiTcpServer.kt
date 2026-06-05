package com.terrsus.terrorwear.features.wifi

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.net.ServerSocket
import java.net.Socket

class WifiTcpServer(
    private val port: Int
) {
    private val server = ServerSocket(port)
    private var client: Socket? = null

    private val incoming = Channel<ByteArray>(Channel.BUFFERED)
    val packets: Flow<ByteArray> = incoming.receiveAsFlow()

    fun start() {
        Thread {
            client = server.accept()
            val input = client!!.getInputStream()

            val buf = ByteArray(2048)
            while (client!!.isConnected) {
                val read = input.read(buf)
                if (read > 0) {
                    incoming.trySend(buf.copyOf(read))
                }
            }
        }.start()
    }

    fun send(data: ByteArray) {
        client?.getOutputStream()?.write(data)
        client?.getOutputStream()?.flush()
    }

    fun stop() {
        client?.close()
        server.close()
    }
}
