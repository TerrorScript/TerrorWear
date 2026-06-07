package com.terrsus.terrorwear.modules.wifi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terrsus.terrorwear.AppContainer
import com.terrsus.terrorwear.domain.wifi.model.WifiPacket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn

class WifiViewModel : ViewModel() {

    private val repo = AppContainer.wifiRepository

    val udpPackets: StateFlow<List<WifiPacket>> =
        repo.udpPackets()
            .runningFold(emptyList<WifiPacket>()) { acc, v -> acc + v }
            .stateIn(viewModelScope, SharingStarted.Companion.Eagerly, emptyList())

    val tcpPackets: StateFlow<List<WifiPacket>> =
        repo.tcpPackets()
            .runningFold(emptyList<WifiPacket>()) { acc, v -> acc + v }
            .stateIn(viewModelScope, SharingStarted.Companion.Eagerly, emptyList())

    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status

    fun sendUdp(text: String, host: String, port: Int) {
        repo.sendUdp(text.encodeToByteArray(), host, port)
        _status.value = "Sent UDP"
    }

    fun sendTcp(text: String) {
        repo.sendTcp(text.encodeToByteArray())
        _status.value = "Sent TCP"
    }

    fun restartUdp(port: Int) {
        repo.startUdp(port)
        _status.value = "UDP restarted"
    }

    fun restartTcp(host: String, port: Int) {
        repo.startTcpClient(host, port)
        _status.value = "TCP restarted"
    }

    fun stopAll() {
        repo.stopAll()
        _status.value = "Stopped"
    }
}