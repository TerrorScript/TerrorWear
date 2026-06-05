package com.terrsus.terrorwear.features.wifi

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

class WifiTcpClient {

    private var socket: Socket? = null
    private var input: InputStream? = null
    private var output: OutputStream? = null

    private val incoming = Channel<ByteArray>(Channel.BUFFERED)
    val packets: Flow<ByteArray> = incoming.receiveAsFlow()

    fun connect(host: String, port: Int) {
        socket = Socket(host, port)
        input = socket!!.getInputStream()
        output = socket!!.getOutputStream()

        Thread {
            val buf = ByteArray(2048)
            while (socket?.isConnected == true) {
                val read = input!!.read(buf)
                if (read > 0) {
                    incoming.trySend(buf.copyOf(read))
                }
            }
        }.start()
    }

    fun send(data: ByteArray) {
        output?.write(data)
        output?.flush()
    }

    fun disconnect() {
        socket?.close()
    }
}
