package com.terrsus.terrorwear.viewmodel.ble

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terrsus.terrorwear.AppContainer
import com.terrsus.terrorwear.features.ble.common.model.BleGattConnectionState
import com.terrsus.terrorwear.features.ble.common.model.BleGattCharacteristicValue
import com.terrsus.terrorwear.features.ble.common.model.BleGattService
import com.terrsus.terrorwear.features.ble.common.util.updateValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class GattViewModel(
    val address: String
) : ViewModel() {

    private val client = AppContainer.bleGattClient

    // Internal mutable state for services
    private val _services = MutableStateFlow<List<BleGattService>>(emptyList())
    val services = _services.asStateFlow()

    // Connection state is already a Flow from the client
    val connectionState = client.connectionState(address)
        .stateIn(viewModelScope, SharingStarted.Companion.Eagerly, BleGattConnectionState.Disconnected)

    init {
        // Start connection immediately
        client.connect(address)

        // Collect service discovery updates
        viewModelScope.launch {
            client.services(address).collect { list ->
                _services.value = list
            }
        }

        // Collect notifications and merge them into the service list
        viewModelScope.launch {
            client.notifications(address).collect { update: BleGattCharacteristicValue ->
                _services.update { old -> old.updateValue(update) }
            }
        }
    }

    fun read(service: UUID, characteristic: UUID): Flow<BleGattCharacteristicValue> =
        client.read(address, service, characteristic)

    fun write(service: UUID, characteristic: UUID, data: ByteArray) =
        client.write(address, service, characteristic, data)

    fun enableNotifications(service: UUID, characteristic: UUID) =
        client.enableNotifications(address, service, characteristic)

    override fun onCleared() {
        client.disconnect(address)
    }
}