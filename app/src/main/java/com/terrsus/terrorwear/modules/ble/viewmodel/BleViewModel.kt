package com.terrsus.terrorwear.viewmodel.ble

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terrsus.terrorwear.AppContainer
import com.terrsus.terrorwear.features.ble.domain.model.BleDevice
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.stateIn

class BleViewModel : ViewModel() {

    private val observeDevices = AppContainer.observeBleDevicesUseCase
    private val observeScanning = AppContainer.observeBleScanningUseCase
    private val startScan = AppContainer.startBleScanUseCase
    private val stopScan = AppContainer.stopBleScanUseCase

    @OptIn(FlowPreview::class)
    val scanResults = observeDevices()
        .map { list -> list.distinctBy { it.address } }
        .sample(250) // emit at most every 250ms
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Companion.Eagerly, emptyList())

    val isScanning: StateFlow<Boolean> =
        observeScanning()
            .stateIn(viewModelScope, SharingStarted.Companion.Eagerly, false)

    private val _selectedDevice = MutableStateFlow<BleDevice?>(null)
    val selectedDevice: StateFlow<BleDevice?> = _selectedDevice

    fun selectDevice(device: BleDevice) {
        _selectedDevice.value = device
    }

    fun beginScan() = startScan()
    fun endScan() = stopScan()

    private val _statusMessage = MutableStateFlow("")
    val statusMessage: StateFlow<String> = _statusMessage

    private val _navigateToGatt = MutableStateFlow<BleDevice?>(null)
    val navigateToGatt: StateFlow<BleDevice?> = _navigateToGatt

    fun navigateToGatt(device: BleDevice) {
        _navigateToGatt.value = device
    }
    fun clearGattNavigation() {
        _navigateToGatt.value = null
    }

    fun showStatus(msg: String) {
        _statusMessage.value = msg
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    val filteredResults: StateFlow<List<BleDevice>> =
        combine(scanResults, searchQuery) { devices, query ->
            if (query.isBlank()) devices
            else devices.filter { d ->
                d.name?.contains(query, ignoreCase = true) == true ||
                        d.address.contains(query, ignoreCase = true)
            }
        }.stateIn(viewModelScope, SharingStarted.Companion.Eagerly, emptyList())

    val deviceCount: StateFlow<Int> =
        filteredResults.map { it.size }
            .stateIn(viewModelScope, SharingStarted.Companion.Eagerly, 0)
}