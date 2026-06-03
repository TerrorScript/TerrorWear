package com.terrsus.terrorwear.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terrsus.terrorwear.AppContainer
import com.terrsus.terrorwear.features.ble.gatt.BleGattConnectionState
import com.terrsus.terrorwear.features.ble.gatt.model.BleGattService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.util.UUID

class GattViewModel(
    val address: String
) : ViewModel() {

    private val client = AppContainer.bleGattClient   // ⭐ FIXED

    val connectionState = client.connectionState(address)
        .stateIn(viewModelScope, SharingStarted.Eagerly, BleGattConnectionState.Disconnected)

    val services = client.services(address)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList<BleGattService>())

    val notifications = client.notifications(address)
        .stateIn(viewModelScope, SharingStarted.Eagerly, ByteArray(0))

    init {
        client.connect(address)
    }

    fun read(service: UUID, characteristic: UUID) =
        client.read(address, service, characteristic)

    fun write(service: UUID, characteristic: UUID, data: ByteArray) =
        client.write(address, service, characteristic, data)

    fun enableNotifications(service: UUID, characteristic: UUID) =
        client.enableNotifications(address, service, characteristic)

    override fun onCleared() {
        client.disconnect(address)
    }
}
