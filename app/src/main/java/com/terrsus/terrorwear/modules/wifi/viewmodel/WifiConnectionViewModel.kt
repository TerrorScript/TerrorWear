package com.terrsus.terrorwear.modules.wifi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terrsus.terrorwear.AppContainer
import com.terrsus.terrorwear.domain.wifi.model.WifiConnectionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WifiConnectionViewModel : ViewModel() {

    private val repo = AppContainer.wifiRepository

    // ---------------------------------------------------------
    // Connection State
    // ---------------------------------------------------------
    private val _wifiState = MutableStateFlow(WifiConnectionState.Disconnected)
    val wifiState: StateFlow<WifiConnectionState> = _wifiState

    // ---------------------------------------------------------
    // Status + Event Log
    // ---------------------------------------------------------
    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status

    private val _eventLog = MutableStateFlow<List<String>>(emptyList())
    val eventLog: StateFlow<List<String>> = _eventLog

    private fun log(message: String) {
        Log.d("TW/WifiConnectionViewModel", message)
        _eventLog.value = _eventLog.value + message
        _status.value = message
    }

    // ---------------------------------------------------------
    // UDP Actions
    // ---------------------------------------------------------
    fun sendUdp(text: String, host: String, port: Int) {
        repo.sendUdp(text.encodeToByteArray(), host, port)
        log("Sent UDP → $host:$port")
    }

    fun restartUdp(port: Int) {
        log("UDP listener starting on :$port")
        repo.startUdp(port)
        log("UDP listener started on :$port")
    }

    // ---------------------------------------------------------
    // TCP Client Actions
    // ---------------------------------------------------------
    fun sendTcp(text: String) {
        log("Sending TCP packet")
        repo.sendTcp(text.encodeToByteArray())
        log("Sent TCP packet")
    }

    fun restartTcp(host: String, port: Int) {
        log("TCP client connecting → $host:$port")
        repo.startTcpClient(host, port)
        log("TCP client connected → $host:$port")
    }

    // ---------------------------------------------------------
    // TCP Server Actions
    // ---------------------------------------------------------
    fun startTcpServer(port: Int) {
        log("TCP server starting on :$port")
        repo.startTcpServer(port)
        log("TCP server started on :$port")
    }

    // ---------------------------------------------------------
    // Stop Everything
    // ---------------------------------------------------------
    fun stopAll() {
        log("Stopped all Wi‑Fi sockets")
        repo.stopAll()
        log("Stopped all Wi‑Fi sockets")
    }

    // ---------------------------------------------------------
    // Future: Ping, mDNS, SSDP, ARP, etc.
    // ---------------------------------------------------------
    fun pingHost(host: String) {
        viewModelScope.launch {
            log("Pinging $host…")
            // placeholder for future ping implementation
        }
    }
}