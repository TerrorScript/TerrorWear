package com.terrsus.terrorwear.modules.wifi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terrsus.terrorwear.domain.wifi.model.WifiConnectionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WifiNetworkInfoViewModel : ViewModel() {

    // These will later be populated by WifiManager integration

    private val _ssid = MutableStateFlow("—")
    val ssid: StateFlow<String> = _ssid

    private val _bssid = MutableStateFlow("—")
    val bssid: StateFlow<String> = _bssid

    private val _rssi = MutableStateFlow("— dBm")
    val rssi: StateFlow<String> = _rssi

    private val _linkSpeed = MutableStateFlow("— Mbps")
    val linkSpeed: StateFlow<String> = _linkSpeed

    private val _frequency = MutableStateFlow("— GHz")
    val frequency: StateFlow<String> = _frequency

    private val _ipAddress = MutableStateFlow("—")
    val ipAddress: StateFlow<String> = _ipAddress

    private val _gateway = MutableStateFlow("—")
    val gateway: StateFlow<String> = _gateway

    private val _dns = MutableStateFlow("—")
    val dns: StateFlow<String> = _dns

    private val _security = MutableStateFlow("—")
    val security: StateFlow<String> = _security
}