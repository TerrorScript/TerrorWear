package com.terrsus.terrorwear.features.wifi.manager

import android.util.Log
import com.terrsus.terrorwear.features.wifi.domain.model.WifiConnectionState
import com.terrsus.terrorwear.features.wifi.domain.model.WifiEvent
import com.terrsus.terrorwear.features.wifi.tcpclient.WifiTcpClient
import com.terrsus.terrorwear.features.wifi.tcpclient.WifiTcpClientFake
import com.terrsus.terrorwear.features.wifi.tcpclient.WifiTcpClientImpl
import com.terrsus.terrorwear.features.wifi.tcpserver.WifiTcpServer
import com.terrsus.terrorwear.features.wifi.tcpserver.WifiTcpServerFake
import com.terrsus.terrorwear.features.wifi.tcpserver.WifiTcpServerImpl
import com.terrsus.terrorwear.features.wifi.udpclient.WifiUdpClient
import com.terrsus.terrorwear.features.wifi.udpclient.WifiUdpClientFake
import com.terrsus.terrorwear.features.wifi.udpclient.WifiUdpClientImpl
import com.terrsus.terrorwear.util.DeviceUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow


/**
 * Default implementation of WifiManager.
 *
 * Manages the lifecycle of UDP, TCP client, and TCP server components.
 * Ensures that only one instance of each component is active at a time.
 * This class performs no asynchronous work and contains no flows.
 */
class WifiManager {

    init {
        Log.i("TW/WifiManager", "init")
    }

    private val _state = MutableStateFlow<WifiConnectionState>(WifiConnectionState.Idle)
    val state: StateFlow<WifiConnectionState> = _state

    private val _events = MutableSharedFlow<WifiEvent>(extraBufferCapacity = 64)
    val events: SharedFlow<WifiEvent> = _events

    private var udpClient: WifiUdpClient? = null
    private var tcpClient: WifiTcpClient? = null
    private var tcpServer: WifiTcpServer? = null

    fun startUdp(port: Int): WifiUdpClient {
        stopUdp()

        val newUdpClient =
            if (DeviceUtils.isEmulator) WifiUdpClientFake(port)
            else WifiUdpClientImpl(port)

        newUdpClient.start()
        udpClient = newUdpClient

        _events.tryEmit(WifiEvent.Connected("udp-listener", port))

        return newUdpClient
    }

    fun startTcpClient(host: String, port: Int): WifiTcpClient {
        stopTcpClient()

        _state.value = WifiConnectionState.Connecting(host, port)

        fun handleEvent(wifiEvent: WifiEvent) {
            when (wifiEvent) {
                is WifiEvent.Connected ->
                    _state.value = WifiConnectionState.Connected(wifiEvent.host, wifiEvent.port)

                is WifiEvent.Error ->
                    _state.value = WifiConnectionState.Error(wifiEvent.message, wifiEvent.cause)

                is WifiEvent.Closed ->
                    _state.value = WifiConnectionState.Closed

                else -> Unit
            }

            _events.tryEmit(wifiEvent)
        }
        val newTcpClient =
            if (DeviceUtils.isEmulator) WifiTcpClientFake(::handleEvent)
            else WifiTcpClientImpl(::handleEvent)

        newTcpClient.connect(host, port)
        tcpClient = newTcpClient

        return newTcpClient
    }

    fun startTcpServer(port: Int): WifiTcpServer {
        stopTcpServer()

        val newTcpServer =
            if (DeviceUtils.isEmulator) WifiTcpServerFake(port)
            else WifiTcpServerImpl(port)

        newTcpServer.start()
        tcpServer = newTcpServer
        return newTcpServer
    }

    fun stopUdp() {
        udpClient?.stop()
        udpClient = null
    }

    fun stopTcpClient() {
        tcpClient?.disconnect()
        tcpClient = null
    }

    fun stopTcpServer() {
        tcpServer?.stop()
        tcpServer = null
    }

    fun stopAll() {
        stopUdp()
        stopTcpClient()
        stopTcpServer()
    }
}