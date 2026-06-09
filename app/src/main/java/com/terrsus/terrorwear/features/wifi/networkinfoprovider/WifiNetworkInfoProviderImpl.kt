package com.terrsus.terrorwear.features.wifi.networkinfoprovider

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.WifiInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.terrsus.terrorwear.features.wifi.domain.model.WifiNetworkInfo

/**
 * Default implementation using ConnectivityManager callbacks.
 *
 * This avoids deprecated WifiManager.connectionInfo and provides
 * real‑time updates when Wi‑Fi capabilities change.
 */
class WifiNetworkInfoProviderImpl(
    private val connectivityManager: ConnectivityManager
) : WifiNetworkInfoProvider {

    private val _networkInfo = MutableStateFlow(
        WifiNetworkInfo(
            ssid = "—",
            bssid = "—",
            rssi = 0,
            linkSpeed = 0,
            frequency = 0,
            ipAddress = "—",
            gateway = "—",
            dns = "—",
            security = "—"
        )
    )
    override val networkInfo = _networkInfo.asStateFlow()

    private var registered = false

    private val callback = object : ConnectivityManager.NetworkCallback() {

        override fun onCapabilitiesChanged(
            network: Network,
            capabilities: NetworkCapabilities
        ) {
            val wifiInfo = capabilities.transportInfo as? WifiInfo ?: return

            _networkInfo.value = WifiNetworkInfo(
                ssid = wifiInfo.ssid ?: "—",
                bssid = wifiInfo.bssid ?: "—",
                rssi = wifiInfo.rssi,
                linkSpeed = wifiInfo.linkSpeed,
                frequency = wifiInfo.frequency,
                ipAddress = formatIp(wifiInfo.ipAddress),
                gateway = "—",   // requires DHCP info (optional)
                dns = "—",       // requires LinkProperties (optional)
                security = "—"   // requires WifiInfo API 33+ (optional)
            )
        }

        override fun onLost(network: Network) {
            _networkInfo.value = WifiNetworkInfo(
                ssid = "—",
                bssid = "—",
                rssi = 0,
                linkSpeed = 0,
                frequency = 0,
                ipAddress = "—",
                gateway = "—",
                dns = "—",
                security = "—"
            )
        }
    }

    override fun start() {
        if (registered) return

        val request = android.net.NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)
        registered = true
    }

    override fun stop() {
        if (!registered) return
        connectivityManager.unregisterNetworkCallback(callback)
        registered = false
    }

    private fun formatIp(ip: Int): String {
        return listOf(
            ip and 0xff,
            ip shr 8 and 0xff,
            ip shr 16 and 0xff,
            ip shr 24 and 0xff
        ).joinToString(".")
    }
}