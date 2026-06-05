package com.terrsus.terrorwear.features.wifi

class WifiManager {

    private var udp: WifiUdpClient? = null
    private var tcp: WifiTcpClient? = null
    private var server: WifiTcpServer? = null

    fun startUdp(port: Int): WifiUdpClient {
        val c = WifiUdpClient(port)
        c.start()
        udp = c
        return c
    }

    fun startTcpClient(host: String, port: Int): WifiTcpClient {
        val c = WifiTcpClient()
        c.connect(host, port)
        tcp = c
        return c
    }

    fun startTcpServer(port: Int): WifiTcpServer {
        val s = WifiTcpServer(port)
        s.start()
        server = s
        return s
    }

    fun stopAll() {
        udp?.stop()
        tcp?.disconnect()
        server?.stop()
    }
}
