package com.terrsus.terrorwear.modules.wifi.ui

import com.terrsus.terrorwear.R

/**
 * Registry of drawable resources used by the Wi‑Fi module UI.
 *
 * Centralizes icon references for WifiInfoScreen, WifiToolsScreen, and
 * WifiLogScreen. This avoids scattering raw drawable IDs throughout the
 * UI layer and simplifies future icon changes.
 */
object WifiIcons {

    /** Icon representing a network ping or reachability test. */
    val Ping = R.drawable.outline_network_ping_24

    /** Icon representing the start of a UDP listener. */
    val UdpStartListening = R.drawable.baseline_hearing_24

    /** Icon representing sending a UDP packet. */
    val UdpSendPacket = R.drawable.baseline_send_24

    /** Icon representing mDNS (multicast DNS) service discovery. */
    val Mdns = R.drawable.baseline_wifi_tethering_24

    /** Icon representing SSDP/UPnP device discovery. */
    val Ssdp = R.drawable.baseline_cast_24

    /** Icon representing a TCP server or listening host. */
    val Server = R.drawable.baseline_dns_24

    /** Icon representing a TCP client or remote endpoint. */
    val Client = R.drawable.outline_computer_24

    /** Icon representing generic packet transmission. */
    val Send = R.drawable.baseline_send_24

    /** Icon representing general tools or utilities. */
    val Tools = R.drawable.baseline_build_24

    /** Icon representing IP or network information. */
    val IP = R.drawable.baseline_wifi_24

    /** Icon representing link speed or throughput. */
    val Speed = R.drawable.baseline_speed_24

    /** Icon representing signal strength (RSSI). */
    val Rssi = R.drawable.baseline_signal_cellular_alt_24

    /** Icon representing SSID or network name. */
    val Ssid = R.drawable.baseline_short_text_24

    /** Icon representing Wi‑Fi connection status. */
    val Status = R.drawable.baseline_wifi_24

    /** Icon representing a list or log view. */
    val List = R.drawable.baseline_list_24

    /** Icon representing backward navigation. */
    val NavigateBack = R.drawable.baseline_arrow_back_ios_new_24
}