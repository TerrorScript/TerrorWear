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

    /**
     * Starts a UDP listener bound to the given [port].
     *
     * This replaces any existing UDP client instance. The connection state is set to
     * [WifiConnectionState.Listening] immediately, and subsequent transport‑level events
     * emitted by the UDP client are forwarded into [events] and reflected in [state].
     *
     * Event handling:
     * - [WifiEvent.Error]: state becomes [WifiConnectionState.Error]
     * - [WifiEvent.Closed]: state becomes [WifiConnectionState.Closed]
     * - Other events: forwarded but do not affect state
     *
     * @return the newly created [WifiUdpClient] instance
     */
    fun startUdp(port: Int): WifiUdpClient {
        stopUdp()

        _state.value = WifiConnectionState.Listening(port)

        fun handleEvent(event: WifiEvent) {
            when (event) {
                is WifiEvent.Error ->
                    _state.value = WifiConnectionState.Error(event.message, event.cause)

                is WifiEvent.Closed ->
                    _state.value = WifiConnectionState.Closed

                else -> Unit
            }

            _events.tryEmit(event)
        }

        val newUdpClient =
            if (DeviceUtils.isEmulator) WifiUdpClientFake(port, ::handleEvent)
            else WifiUdpClientImpl(port, ::handleEvent)

        newUdpClient.start()
        udpClient = newUdpClient
        return newUdpClient
    }

    /**
     * Starts a TCP client connection to the given remote [host] and [port].
     *
     * Any existing TCP client is stopped before creating a new one. The connection state
     * is set to [WifiConnectionState.Connecting] immediately. The underlying TCP client
     * emits transport‑level [WifiEvent]s, which are forwarded into [events] and used to
     * update [state].
     *
     * Event handling:
     * - [WifiEvent.Connected]: state becomes [WifiConnectionState.Connected]
     * - [WifiEvent.Error]: state becomes [WifiConnectionState.Error]
     * - [WifiEvent.Closed]: state becomes [WifiConnectionState.Closed]
     * - Other events: forwarded but do not affect state
     *
     * @return the newly created and connecting [WifiTcpClient] instance
     */
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

    /**
     * Starts a TCP server listening on the given [port].
     *
     * Any existing TCP server is stopped before creating a new one. The connection state
     * is set to [WifiConnectionState.Listening] immediately. The server emits transport‑level
     * [WifiEvent]s, which are forwarded into [events] and used to update [state].
     *
     * Event handling:
     * - [WifiEvent.Connected]: a remote client connected; state becomes
     *   [WifiConnectionState.ClientConnected]
     * - [WifiEvent.Error]: state becomes [WifiConnectionState.Error]
     * - [WifiEvent.Closed]: state becomes [WifiConnectionState.Closed]
     * - Other events: forwarded but do not affect state
     *
     * @return the newly created and listening [WifiTcpServer] instance
     */
    fun startTcpServer(port: Int): WifiTcpServer {
        stopTcpServer()

        _state.value = WifiConnectionState.Listening(port)

        fun handleEvent(event: WifiEvent) {
            when (event) {
                is WifiEvent.Connected ->
                    _state.value = WifiConnectionState.ClientConnected(event.host, event.port)

                is WifiEvent.Error ->
                    _state.value = WifiConnectionState.Error(event.message, event.cause)

                is WifiEvent.Closed ->
                    _state.value = WifiConnectionState.Closed

                else -> Unit
            }

            _events.tryEmit(event)
        }

        val newTcpServer =
            if (DeviceUtils.isEmulator) WifiTcpServerFake(port, ::handleEvent)
            else WifiTcpServerImpl(port, ::handleEvent)

        newTcpServer.start()
        tcpServer = newTcpServer
        return newTcpServer
    }

    /**
     * Stops the active UDP client, if any, and clears the reference.
     * Does not modify [state]; the transport layer is responsible for emitting
     * [WifiEvent.Closed] if appropriate.
     */
    fun stopUdp() {
        udpClient?.stop()
        udpClient = null
    }

    /**
     * Disconnects the active TCP client, if any, and clears the reference.
     * Does not modify [state]; the transport layer is responsible for emitting
     * [WifiEvent.Closed] if appropriate.
     */
    fun stopTcpClient() {
        tcpClient?.disconnect()
        tcpClient = null
    }

    /**
     * Stops the active TCP server, if any, and clears the reference.
     * Does not modify [state]; the transport layer is responsible for emitting
     * [WifiEvent.Closed] if appropriate.
     */
    fun stopTcpServer() {
        tcpServer?.stop()
        tcpServer = null
    }

    /**
     * Stops all active Wi‑Fi transport components (UDP, TCP client, TCP server).
     * This is equivalent to calling [stopUdp], [stopTcpClient], and [stopTcpServer].
     */
    fun stopAll() {
        stopUdp()
        stopTcpClient()
        stopTcpServer()
    }
}