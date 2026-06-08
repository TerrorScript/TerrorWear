package com.terrsus.terrorwear.modules.wifi.ui

import com.terrsus.terrorwear.R

/**
 * Centralized registry of drawable resources used by the Wi‑Fi module UI.
 *
 * This object provides a single source of truth for all icons used across
 * WifiInfoScreen, WifiToolsScreen, and WifiLogScreen. Using a dedicated
 * lookup object keeps the UI layer clean, avoids scattering raw drawable
 * references throughout the codebase, and makes future icon changes trivial.
 *
 * Icons here are strictly presentation‑layer concerns and therefore belong
 * in the `ui` package rather than domain or data layers.
 */
object WifiIcons {

    /** Icon representing a network ping or reachability test. */
    val Ping = R.drawable.outline_network_ping_24

    /** Icon representing mDNS (multicast DNS) service discovery. */
    val Mdns = R.drawable.baseline_wifi_tethering_24

    /** Icon representing SSDP/UPnP device discovery on the local network. */
    val Ssdp = R.drawable.baseline_cast_24

    /** Icon representing a TCP server or listening host. */
    val Server = R.drawable.baseline_dns_24

    /** Icon representing a TCP client or remote endpoint. */
    val Client = R.drawable.outline_computer_24

    /** Icon representing sending a packet (UDP or TCP). */
    val Send = R.drawable.baseline_send_24

    /** Icon representing general tools or utilities. */
    val Tools = R.drawable.baseline_build_24

    /** Icon representing IP/network information. */
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
}