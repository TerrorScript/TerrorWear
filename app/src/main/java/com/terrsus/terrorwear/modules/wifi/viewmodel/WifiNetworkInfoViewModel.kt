package com.terrsus.terrorwear.modules.wifi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terrsus.terrorwear.AppContainer
import com.terrsus.terrorwear.features.wifi.networkinfoprovider.WifiNetworkInfoProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ViewModel exposing current Wi‑Fi network information for UI consumption.
 *
 * This ViewModel observes WifiNetworkInfoProvider and maps its domain‑level
 * WifiNetworkInfo snapshots into UI‑ready string values. It performs no
 * platform operations and contains no Android Wi‑Fi API usage.
 *
 * The provider is started when observation begins and stopped when the
 * ViewModel is cleared. UI screens should call startObserving() when they
 * become active.
 */
class WifiNetworkInfoViewModel() : ViewModel() {
    private val wifiNetworkInfoProvider = AppContainer.wifiNetworkInfoProvider

    private val _ssid = MutableStateFlow("—")
    /** Currently connected Wi‑Fi SSID as a UI‑formatted string. */
    val ssid: StateFlow<String> = _ssid

    private val _bssid = MutableStateFlow("—")
    /** Currently connected Wi‑Fi BSSID (AP MAC address).
     */
    val bssid: StateFlow<String> = _bssid

    private val _rssi = MutableStateFlow("— dBm")
    /** Received signal strength indicator formatted as a string with dBm suffix. */
    val rssi: StateFlow<String> = _rssi

    private val _linkSpeed = MutableStateFlow("— Mbps")
    /** Link speed of the current Wi‑Fi connection formatted in Mbps. */
    val linkSpeed: StateFlow<String> = _linkSpeed

    private val _frequency = MutableStateFlow("— GHz")
    /** Center frequency of the current Wi‑Fi channel formatted in GHz. */
    val frequency: StateFlow<String> = _frequency

    private val _ipAddress = MutableStateFlow("—")
    /** Local IP address assigned to the device on the Wi‑Fi network. */
    val ipAddress: StateFlow<String> = _ipAddress

    private val _gateway = MutableStateFlow("—")
    /** Gateway (router) IP address for the current Wi‑Fi network. */
    val gateway: StateFlow<String> = _gateway

    private val _dns = MutableStateFlow("—")
    /** DNS server address used by the current Wi‑Fi network. */
    val dns: StateFlow<String> = _dns

    private val _security = MutableStateFlow("—")
    /** Security type of the current Wi‑Fi network (e.g., WPA2, WPA3). */
    val security: StateFlow<String> = _security

    /**
     * Begins observing WifiNetworkInfoProvider for updates.
     *
     * The provider is activated here. Collected values are transformed into
     * formatted UI strings. This method should be invoked by the UI layer
     * when the corresponding screen becomes visible.
     */
    fun startObserving() {
        wifiNetworkInfoProvider.start()

        viewModelScope.launch {
            wifiNetworkInfoProvider.networkInfo.collectLatest { info ->
                _ssid.value = info.ssid
                _bssid.value = info.bssid
                _rssi.value = "${info.rssi} dBm"
                _linkSpeed.value = "${info.linkSpeed} Mbps"

                // Convert MHz → GHz with one decimal precision
                val ghz = info.frequency / 1000.0
                _frequency.value = String.format("%.1f GHz", ghz)

                _ipAddress.value = info.ipAddress
                _gateway.value = info.gateway
                _dns.value = info.dns
                _security.value = info.security
            }
        }
    }

    /**
     * Stops the provider when the ViewModel is destroyed.
     *
     * This ensures that callbacks and background work are terminated when
     * the UI no longer requires network information.
     */
    override fun onCleared() {
        wifiNetworkInfoProvider.stop()
        super.onCleared()
    }
}
