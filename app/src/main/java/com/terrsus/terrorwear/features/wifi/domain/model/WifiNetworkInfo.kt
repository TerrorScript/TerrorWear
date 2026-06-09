package com.terrsus.terrorwear.features.wifi.domain.model

/**
 * Snapshot of current Wi‑Fi network information.
 */
data class WifiNetworkInfo(
    val ssid: String,
    val bssid: String,
    val rssi: Int,
    val linkSpeed: Int,
    val frequency: Int,
    val ipAddress: String,
    val gateway: String,
    val dns: String,
    val security: String
)