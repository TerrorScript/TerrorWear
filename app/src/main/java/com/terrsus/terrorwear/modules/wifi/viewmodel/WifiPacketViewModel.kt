package com.terrsus.terrorwear.modules.wifi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terrsus.terrorwear.AppContainer
import com.terrsus.terrorwear.domain.wifi.model.WifiPacket
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WifiPacketViewModel : ViewModel() {

    private val repo = AppContainer.wifiRepository

    /** UDP packet stream */
    val udpPackets: StateFlow<List<WifiPacket>> =
        repo.udpPackets()
            .runningFold(emptyList<WifiPacket>()) { acc, packet -> acc + packet }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /** TCP client packet stream */
    val tcpPackets: StateFlow<List<WifiPacket>> =
        repo.tcpPackets()
            .runningFold(emptyList<WifiPacket>()) { acc, packet -> acc + packet }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /** TCP server packet stream */
    val tcpServerPackets: StateFlow<List<WifiPacket>> =
        repo.tcpServerPackets()
            .runningFold(emptyList<WifiPacket>()) { acc, packet -> acc + packet }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /** Unified mixed timeline */
    val allPackets: StateFlow<List<WifiPacket>> =
        merge(
            repo.udpPackets(),
            repo.tcpPackets(),
            repo.tcpServerPackets()
        )
            .runningFold(emptyList<WifiPacket>()) { acc, packet ->
                (acc + packet).sortedBy { it.timestamp }
            }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}