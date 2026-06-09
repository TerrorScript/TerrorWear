package com.terrsus.terrorwear.features.wifi.manager

import android.util.Log
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
        return newUdpClient
    }

    fun startTcpClient(host: String, port: Int): WifiTcpClient {
        stopTcpClient()

        val newTcpClient =
            if (DeviceUtils.isEmulator) WifiTcpClientFake()
            else WifiTcpClientImpl()

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