package com.terrsus.terrorwear.features.wifi

/**
 * Low‑level Wi‑Fi subsystem manager.
 *
 * This class owns the lifecycle of all Wi‑Fi communication primitives:
 * - UDP client
 * - TCP client
 * - TCP server
 *
 * It provides explicit start/stop semantics and ensures that only one instance
 * of each component is active at a time. Higher‑level reactive behavior
 * (Flows, state tracking, event streams) is implemented in WifiRepository.
 *
 * This class intentionally contains no coroutines or flows; it is a
 * deterministic, synchronous lifecycle owner for socket components.
 */
class WifiManager {

    private var udpClient: WifiUdpClient? = null
    private var tcpClient: WifiTcpClient? = null
    private var tcpServer: WifiTcpServer? = null

    /**
     * Starts a new UDP listener on the given port.
     * If an existing UDP client is running, it is stopped and replaced.
     */
    fun startUdp(port: Int): WifiUdpClient {
        stopUdp()

        val newUdpClient = WifiUdpClient(port)
        newUdpClient.start()
        udpClient = newUdpClient

        return newUdpClient
    }

    /**
     * Starts a new TCP client and connects it to the given host and port.
     * If an existing TCP client is running, it is disconnected and replaced.
     */
    fun startTcpClient(host: String, port: Int): WifiTcpClient {
        stopTcpClient()

        val newTcpClient = WifiTcpClient()
        newTcpClient.connect(host, port)
        tcpClient = newTcpClient

        return newTcpClient
    }

    /**
     * Starts a new TCP server on the given port.
     * If an existing server is running, it is stopped and replaced.
     */
    fun startTcpServer(port: Int): WifiTcpServer {
        stopTcpServer()

        val newTcpServer = WifiTcpServer(port)
        newTcpServer.start()
        tcpServer = newTcpServer

        return newTcpServer
    }

    /**
     * Stops the currently running UDP client, if any.
     */
    fun stopUdp() {
        udpClient?.stop()
        udpClient = null
    }

    /**
     * Disconnects the currently running TCP client, if any.
     */
    fun stopTcpClient() {
        tcpClient?.disconnect()
        tcpClient = null
    }

    /**
     * Stops the currently running TCP server, if any.
     */
    fun stopTcpServer() {
        tcpServer?.stop()
        tcpServer = null
    }

    /**
     * Stops all Wi‑Fi communication components.
     * This is safe to call at any time.
     */
    fun stopAll() {
        stopUdp()
        stopTcpClient()
        stopTcpServer()
    }
}