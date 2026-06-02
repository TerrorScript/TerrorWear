package com.terrsus.terrorwear.features.wifi

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class WifiUdpClient {

    private val _incoming = MutableSharedFlow<ByteArray>()
    val incoming: SharedFlow<ByteArray> = _incoming

    fun send(data: ByteArray) {
        // TODO: real UDP send
    }

    suspend fun simulateIncoming(data: ByteArray) {
        _incoming.emit(data)
    }
}
