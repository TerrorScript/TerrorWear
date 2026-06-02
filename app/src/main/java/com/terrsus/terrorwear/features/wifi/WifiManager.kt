package com.terrsus.terrorwear.features.wifi

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class WifiManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _connected = MutableStateFlow(false)
    val connected: StateFlow<Boolean> = _connected

    private val udp = WifiUdpClient()

    fun start() {
        // TODO: connect to WiFi device
        _connected.value = true
    }

    fun stop() {
        scope.coroutineContext.cancelChildren()
        _connected.value = false
    }

    fun sendUdp(data: ByteArray) {
        udp.send(data)
    }

    fun observeUdp(): Flow<ByteArray> = udp.incoming
}
